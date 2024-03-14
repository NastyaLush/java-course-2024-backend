package edu.java.service.jdbc;

import edu.java.model.LinkResponse;
import edu.java.repository.dto.ChatDTO;
import edu.java.repository.dto.TrackingUrlsDeleteDTO;
import edu.java.repository.dto.TrackingUrlsInputDTO;
import edu.java.repository.dto.UrlDTO;
import edu.java.repository.dto.UrlInputDTO;
import edu.java.repository.jdbc.JdbcTgChatRepository;
import edu.java.repository.jdbc.JdbcTrackingUrlsRepository;
import edu.java.repository.jdbc.JdbcUrlRepository;
import edu.java.service.UrlService;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcUrlService implements UrlService {
    private final JdbcUrlRepository jdbcUrlRepository;
    private final JdbcTrackingUrlsRepository jdbcTrackingUrlsRepository;
    private final JdbcTgChatRepository jdbcTgChatRepository;


    @Override
    @Transactional
    public LinkResponse add(long tgChatId, URI url) {
        long id = jdbcUrlRepository.add(new UrlInputDTO(url.toString(), ZonedDateTime.now(), ZonedDateTime.now()));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInputDTO(tgChatId, id));
        return new LinkResponse().id(id).url(url);
    }

    @Override
    public LinkResponse remove(long tgChatId, URI url) {
        long id = jdbcTrackingUrlsRepository.remove(new TrackingUrlsDeleteDTO(tgChatId, url));
        return new LinkResponse().id(id).url(url);
    }

    @Override
    public List<UrlDTO> listAll(long tgChatId) {
        ChatDTO chatDTO = jdbcTgChatRepository.findById(tgChatId);
        return jdbcTrackingUrlsRepository.findByTgId(chatDTO.chatId()).stream().map(trackingUrlsDTO -> jdbcUrlRepository.findById(trackingUrlsDTO.urlId())).toList();
    }

    @Override
    public List<UrlDTO> findNotCheckedForLongTime(ZonedDateTime max_last_check) {
        return jdbcUrlRepository.findNotCheckedForLongTime(max_last_check);
    }
}
