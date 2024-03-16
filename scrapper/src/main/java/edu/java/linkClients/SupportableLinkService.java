package edu.java.linkClients;

import edu.java.bot.model.LinkUpdate;
import java.time.OffsetDateTime;
import java.util.Optional;

public interface SupportableLinkService {
    String getDomain();

    Optional<LinkUpdateResponse> getLastUpdateDate(String pathOfUrl, OffsetDateTime lastUpdate);

}
