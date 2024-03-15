package edu.java.linkClients;

import java.time.OffsetDateTime;

public interface SupportableLinkService {
    String getDomain();

    OffsetDateTime getLastUpdateDate(String pathOfUrl);
}
