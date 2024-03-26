package edu.java.util;

import edu.java.client.linkClients.LinkUpdateResponse;
import edu.java.client.linkClients.SupportableLinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class LinkManager {
    private final List<SupportableLinkService> supportableLinkServices;

    @Autowired
    public LinkManager(List<SupportableLinkService> supportableLinkServices) {
        this.supportableLinkServices = supportableLinkServices;
    }

    public void validateURI(URI uri) {
        Optional<SupportableLinkService> linkService =
                supportableLinkServices.stream()
                                       .filter(linkClient -> linkClient.getDomain()
                                                                       .equals(uri.getAuthority()))
                                       .findFirst();
        if (linkService.isEmpty()) {
            throw new IllegalArgumentException("this url is not supported");
        }
    }

    public LinkUpdateResponse getLastUpdate(URI uri, OffsetDateTime lastUpdate) {

        Optional<SupportableLinkService> linkService = getLinkService(uri);
        if (linkService.isEmpty()) {
            log.warn("this type of url is not supported");
            throw new IllegalArgumentException("this type of url" + uri + " is not supported");
        }
        return linkService.get()
                          .getLastUpdateDate(uri.getPath(), lastUpdate);

    }

    private Optional<SupportableLinkService> getLinkService(URI uri) {
        return supportableLinkServices.stream()
                                      .filter(linkClient -> linkClient.getDomain()
                                                                      .equals(uri.getAuthority()))
                                      .findFirst();

    }

    public boolean shouldUpdate(OffsetDateTime lastUpdate, OffsetDateTime newLastUpdate) {
        return newLastUpdate.isAfter(lastUpdate);
    }
}
