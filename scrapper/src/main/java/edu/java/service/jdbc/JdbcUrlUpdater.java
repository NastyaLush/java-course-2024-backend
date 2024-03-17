package edu.java.service.jdbc;

import edu.java.bot.api.UpdatesApi;
import edu.java.bot.model.LinkUpdate;
import edu.java.configuration.ApplicationConfig;
import edu.java.linkClients.LinkUpdateResponse;
import edu.java.linkClients.SupportableLinkService;
import edu.java.repository.entity.ChatEntity;
import edu.java.service.UrlService;
import edu.java.service.UrlUpdater;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class JdbcUrlUpdater implements UrlUpdater {
    private final UrlService urlService;
    private final ApplicationConfig applicationConfig;
    private final List<SupportableLinkService> supportableLinkServices;
    private final UpdatesApi updatesApi;

    @Autowired
    public JdbcUrlUpdater(
        UrlService urlService,
        ApplicationConfig applicationConfig,
        List<SupportableLinkService> supportableLinkServices,
        UpdatesApi updatesApi
    ) {
        this.urlService = urlService;
        this.applicationConfig = applicationConfig;
        this.supportableLinkServices = supportableLinkServices;
        this.updatesApi = updatesApi;
    }

    @Override
    public int update() {
        AtomicLong counter = new AtomicLong();
        urlService.findNotCheckedForLongTime(OffsetDateTime.now().minus(
            applicationConfig.scheduler().interval())).forEach(
            urlDTO -> {

                try {
                    URI uri = new URI(urlDTO.url());

                    Optional<SupportableLinkService> supportableLinkService =
                        supportableLinkServices.stream()
                                               .filter(linkClient -> linkClient.getDomain().equals(uri.getAuthority()))
                                               .findFirst();
                    if (supportableLinkService.isEmpty()) {
                        throw new RuntimeException("this type of url is not supported");
                    }
                    Optional<LinkUpdateResponse> linkUpdateResponse =
                        supportableLinkService.get().getLastUpdateDate(uri.getPath(), urlDTO.lastUpdate());
                    if (linkUpdateResponse.isPresent()) {
                        urlService.update(urlDTO.id(), OffsetDateTime.now(), linkUpdateResponse.get().lastUpdate());

                        LinkUpdate linkUpdate =
                            new LinkUpdate().id(urlDTO.id()).url(uri)
                                            .description(linkUpdateResponse.get().description())
                                            .tgChatIds(urlService.getChats(urlDTO.id()).stream().map(
                                                ChatEntity::tgChatId).toList());
                        updatesApi.updatesPost(linkUpdate);
                        counter.getAndIncrement();
                    } else {
                        urlService.update(urlDTO.id(), OffsetDateTime.now());
                    }
                } catch (URISyntaxException e) {
                    throw new RuntimeException("impossible to parse uri");
                }

            }
        );
        log.info("update method");
        return counter.intValue();
    }
}
