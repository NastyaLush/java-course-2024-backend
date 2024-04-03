package edu.java.client.linkClients;

import edu.java.exception.CustomWebClientException;
import java.time.OffsetDateTime;

public interface SupportableLinkService {
    String getDomain();

    LinkUpdateResponse getLastUpdateDate(String pathOfUrl, OffsetDateTime lastUpdate) throws CustomWebClientException;

}
