package edu.java.service;

import edu.java.repository.dto.UrlDTO;
import java.net.URI;
import java.util.List;

public interface UrlService {
    int add(int tgChatId, URI url);

    void remove(int tgChatId, URI url);

    List<UrlDTO> listAll(int tgChatId);
}
