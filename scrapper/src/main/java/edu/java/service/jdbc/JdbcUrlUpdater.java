package edu.java.service.jdbc;

import edu.java.bot.api.UpdatesApi;
import edu.java.bot.model.LinkUpdate;
import edu.java.configuration.ApplicationConfig;
import edu.java.entity.ChatEntity;
import edu.java.entity.UrlEntity;
import edu.java.linkClients.SupportableLinkService;
import edu.java.service.UrlService;
import edu.java.service.UrlUpdater;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
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
    public void update() {

        getNotCheckedForLingTimeUrls().forEach(
            urlEntity -> {

                try {
                    URI uri = new URI(urlEntity.url());

                    Optional<SupportableLinkService> linkService = getLinkService(uri);
                    if (linkService.isEmpty()) {
                        log.warn("this type of url is not supported");
                        throw new IllegalArgumentException("this type of url" + uri + " is not supported");
                    }
                    OffsetDateTime lastUpdateDate = linkService.get().getLastUpdateDate(uri.getPath());
                    update(lastUpdateDate, urlEntity, uri);


                } catch (URISyntaxException e) {
                    log.warn("impossible to parse uri");
                } catch (IllegalArgumentException e) {
                    log.warn(e.getMessage());
                    urlService.remove(URI.create(urlEntity.url()));
                    log.warn(urlEntity.url() + " was deleted");
                }

            }
        );
        log.info("update method");
    }

    private List<UrlEntity> getNotCheckedForLingTimeUrls() {
        return urlService.findNotCheckedForLongTime(OffsetDateTime.now().minus(
            applicationConfig.scheduler().interval()));
    }

    private Optional<SupportableLinkService> getLinkService(URI uri) {
        return supportableLinkServices.stream()
                                      .filter(linkClient -> linkClient.getDomain().equals(uri.getAuthority()))
                                      .findFirst();

    }

    private void update(OffsetDateTime lastUpdateDate, UrlEntity urlEntity, URI uri) {
        if (lastUpdateDate.isAfter(urlEntity.lastUpdate())) {
            urlService.update(urlEntity.id(), OffsetDateTime.now(), lastUpdateDate);

            List<Long> tgChatIdsWithThisUrl = urlService.getChats(urlEntity.id()).stream().map(
                ChatEntity::tgChatId).toList();
            LinkUpdate linkUpdate =
                new LinkUpdate().id(urlEntity.id()).url(uri)
                                .description("something changed")
                                .tgChatIds(tgChatIdsWithThisUrl);
            updatesApi.updatesPost(linkUpdate);
        } else {
            urlService.update(urlEntity.id(), OffsetDateTime.now());
        }
    }
}
