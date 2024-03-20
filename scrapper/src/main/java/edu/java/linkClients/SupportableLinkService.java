package edu.java.linkClients;

import edu.java.exceptions.WebClientException;
import java.time.OffsetDateTime;

public interface SupportableLinkService {
    String getDomain();

    LinkUpdateResponse getLastUpdateDate(String pathOfUrl, OffsetDateTime lastUpdate) throws WebClientException;

}
