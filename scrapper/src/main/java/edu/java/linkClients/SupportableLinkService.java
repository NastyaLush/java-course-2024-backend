package edu.java.linkClients;

import java.time.OffsetDateTime;

public interface SupportableLinkService {
    String getDomain();

    LinkUpdateResponse getLastUpdateDate(String pathOfUrl, OffsetDateTime lastUpdate);

}
