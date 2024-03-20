package edu.java.service.abstractImplementation;

import edu.java.bot.api.UpdatesApi;
import edu.java.bot.model.LinkUpdate;
import edu.java.configuration.ApplicationConfig;
import edu.java.entity.ChatEntity;
import edu.java.entity.UrlEntity;
import edu.java.exceptions.WebClientException;
import edu.java.linkClients.LinkUpdateResponse;
import edu.java.service.UrlService;
import edu.java.service.UrlUpdater;
import edu.java.util.LinkManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
public class AbstractUrlUpdater implements UrlUpdater {
    private final UrlService urlService;
    private final ApplicationConfig applicationConfig;
    private final UpdatesApi updatesApi;
    private final LinkManager linkManager;

    @Override
    public void update() {

        getNotCheckedForLingTimeUrls().forEach(
                urlEntity -> {

                    try {
                        LinkUpdateResponse linkUpdateResponse = linkManager.getLastUpdate(
                                new URI(urlEntity.getUrl()),
                                urlEntity.getLastUpdate());
                        update(linkUpdateResponse, urlEntity);

                    } catch (URISyntaxException e) {
                        log.warn("impossible to parse uri");
                    } catch (IllegalArgumentException e) {
                        log.warn(e.getMessage());
                        urlService.remove(URI.create(urlEntity.getUrl()));
                        log.warn(urlEntity.getUrl() + " was deleted");
                    } catch (WebClientException e){
                        log.warn(e.getMessage());
                    }

                }
        );
        log.info("update method");
    }

    private List<UrlEntity> getNotCheckedForLingTimeUrls() {
        return urlService.findNotCheckedForLongTime(OffsetDateTime.now()
                                                                  .minus(
                                                                          applicationConfig.scheduler()
                                                                                           .interval()));
    }


    private void update(LinkUpdateResponse linkUpdateResponse, UrlEntity urlEntity) {

        if (linkManager.shouldUpdate(urlEntity.getLastUpdate(), linkUpdateResponse.lastUpdate())) {
            urlService.update(urlEntity.getId(), OffsetDateTime.now(), linkUpdateResponse.lastUpdate());

            LinkUpdate linkUpdate =
                    new LinkUpdate().id(urlEntity.getId())
                                    .url(URI.create(urlEntity.getUrl()))
                                    .description(linkUpdateResponse.description())
                                    .tgChatIds(urlService.getChats(urlEntity.getId())
                                                         .stream()
                                                         .map(
                                                                 ChatEntity::getId)
                                                         .toList());
            updatesApi.updatesPost(linkUpdate);
        } else {
            urlService.update(urlEntity.getId(), OffsetDateTime.now());
        }
    }
}
