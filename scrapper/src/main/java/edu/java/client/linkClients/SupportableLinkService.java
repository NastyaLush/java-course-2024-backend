package edu.java.client.linkClients;

import com.example.exceptions.CustomWebClientException;
import java.time.OffsetDateTime;

public interface SupportableLinkService {
    String getDomain();

    LinkUpdateResponse getLastUpdateDate(String pathOfUrl, OffsetDateTime lastUpdate) throws CustomWebClientException;

}
