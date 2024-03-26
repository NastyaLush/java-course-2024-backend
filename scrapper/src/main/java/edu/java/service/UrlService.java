package edu.java.service;

import edu.java.entity.ChatEntity;
import edu.java.entity.UrlEntity;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface UrlService {
    LinkResponse add(long tgChatId, URI url);

    LinkResponse remove(long tgChatId, URI url);

    LinkResponse remove(URI url);

    void update(Long id, OffsetDateTime lastCheck);

    void update(Long id, OffsetDateTime lastCheck, OffsetDateTime lastUpdate);

    List<ChatEntity> getChats(Long urlId);

    ListLinksResponse listAll(long tgChatId);

    List<UrlEntity> findNotCheckedForLongTime(OffsetDateTime maxLastCheck);

}
