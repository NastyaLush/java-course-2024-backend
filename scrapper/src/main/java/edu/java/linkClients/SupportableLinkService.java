package edu.java.linkClients;

import edu.java.exceptions.CustomWebClientException;
import java.time.OffsetDateTime;

public interface SupportableLinkService {
    String getDomain();

    LinkUpdateResponse getLastUpdateDate(String pathOfUrl, OffsetDateTime lastUpdate) throws CustomWebClientException;

}
