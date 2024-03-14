package edu.java.service;

import edu.java.model.LinkResponse;
import edu.java.repository.dto.UrlDTO;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;

public interface UrlService {
    LinkResponse add(long tgChatId, URI url);

    LinkResponse remove(long tgChatId, URI url);

    List<UrlDTO> listAll(long tgChatId);
    List<UrlDTO> findNotCheckedForLongTime(ZonedDateTime max_last_check);


}
