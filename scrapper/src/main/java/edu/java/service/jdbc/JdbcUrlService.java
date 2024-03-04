package edu.java.service.jdbc;

import edu.java.repository.dto.TrackingUrlsInputDTO;
import edu.java.repository.dto.UrlDTO;
import edu.java.repository.dto.UrlInputDTO;
import edu.java.repository.jdbc.JdbcTrackingUrlsRepository;
import edu.java.repository.jdbc.JdbcUrlRepository;
import edu.java.service.UrlService;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcUrlService implements UrlService {
    private final JdbcUrlRepository jdbcUrlRepository;
    private final JdbcTrackingUrlsRepository jdbcTrackingUrlsRepository;

    @Autowired
    public JdbcUrlService(JdbcUrlRepository jdbcUrlRepository, JdbcTrackingUrlsRepository jdbcTrackingUrlsRepository) {
        this.jdbcUrlRepository = jdbcUrlRepository;
        this.jdbcTrackingUrlsRepository = jdbcTrackingUrlsRepository;
    }

    @Override
    public int add(int tgChatId, URI url) {
        //todo: last update
        int key = jdbcUrlRepository.add(new UrlInputDTO(url.toString(), ZonedDateTime.now(), ZonedDateTime.now()));
        return jdbcTrackingUrlsRepository.add(new TrackingUrlsInputDTO(tgChatId, key));
    }

    @Override
    public void remove(int tgChatId, URI url) {
        int key = jdbcUrlRepository.add(new UrlInputDTO(url.toString(), ZonedDateTime.now(), ZonedDateTime.now()));
        jdbcTrackingUrlsRepository.remove(new TrackingUrlsInputDTO(tgChatId, key));
    }

    @Override
    public List<UrlDTO> listAll(int tgChatId) {
        //todo update findAll
//        return jdbcTrackingUrlsRepository.findAll();
        return null;
    }
}
