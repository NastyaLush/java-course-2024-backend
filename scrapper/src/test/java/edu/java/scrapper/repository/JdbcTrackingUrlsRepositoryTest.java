package edu.java.scrapper.repository;

import edu.java.entity.TrackingUrlsDelete;
import edu.java.entity.TrackingUrlsEntity;
import edu.java.entity.TrackingUrlsInput;
import edu.java.entity.UrlInput;
import edu.java.repository.jdbc.JdbcTgChatRepository;
import edu.java.repository.jdbc.JdbcTrackingUrlsRepository;
import edu.java.repository.jdbc.JdbcUrlRepository;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JdbcTrackingUrlsRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcTrackingUrlsRepository jdbcTrackingUrlsRepository;
    @Autowired
    private JdbcTgChatRepository jdbcTgChatRepository;
    @Autowired
    private JdbcUrlRepository jdbcUrlRepository;

    @Test
    @Transactional
    @Rollback
    void add_shouldCorrectlyAddTrackingUrl() {
        long chatKey = jdbcTgChatRepository.add(1);
        long key =
                jdbcUrlRepository.add(new UrlInput("https://www.google.com", OffsetDateTime.now(), OffsetDateTime.now()));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInput(chatKey, key));
        assert jdbcTrackingUrlsRepository.findAll()
                                         .size() == 1;
    }

    @Test
    @Transactional
    @Rollback
    void remove_shouldCorrectlyRemoveTrackingUrl() {
        long chatKey = jdbcTgChatRepository.add(1);
        long key =
                jdbcUrlRepository.add(new UrlInput("https://www.google.com", OffsetDateTime.now(), OffsetDateTime.now()));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInput(chatKey, key));
        jdbcTrackingUrlsRepository.remove(new TrackingUrlsDelete(chatKey, key));
        assert jdbcTrackingUrlsRepository.findAll()
                                         .isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findAll_shouldCorrectlyFindAllTrackingUrls() {
        long chatKey1 = jdbcTgChatRepository.add(1);
        long chatKey2 = jdbcTgChatRepository.add(2);
        long key =
                jdbcUrlRepository.add(new UrlInput("https://www.google.com", OffsetDateTime.now(), OffsetDateTime.now()));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInput(chatKey1, key));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInput(chatKey2, key));
        assert jdbcTrackingUrlsRepository.findAll()
                                         .size() == 2;
    }

    @Test
    @Rollback
    @Transactional
    void findAll_shouldReturnEmptyListIfNoTrackingUrls() {
        assert jdbcTrackingUrlsRepository.findAll()
                                         .isEmpty();
    }

    @Test
    @Rollback
    @Transactional
    void findByChatId_shouldCorrectlyReturnValuesWithThisChatId() {
        long chatId = jdbcTgChatRepository.add(1);
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInput(chatId,
                jdbcUrlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()))));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInput(chatId,
                jdbcUrlRepository.add(new UrlInput("url2", OffsetDateTime.now(), OffsetDateTime.now()))));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInput(chatId,
                jdbcUrlRepository.add(new UrlInput("url3", OffsetDateTime.now(), OffsetDateTime.now()))));
        List<TrackingUrlsEntity> urlsRepositoryByChatId = jdbcTrackingUrlsRepository.findByChatId(chatId);
        assert urlsRepositoryByChatId.size() == 3;
    }

    @Test
    @Rollback
    @Transactional
    void findByChatId_shouldReturnEmptyListIfNoTrackingUrls() {
        long chatId = jdbcTgChatRepository.add(1);
        List<TrackingUrlsEntity> urlsRepositoryByChatId = jdbcTrackingUrlsRepository.findByChatId(chatId);
        assert urlsRepositoryByChatId.isEmpty();
    }

    @Test
    @Rollback
    @Transactional
    void findByUrlId_shouldCorrectlyReturnValuesWithThisUrlId() {
        long urlId = jdbcUrlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInput(jdbcTgChatRepository.add(1), urlId));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInput(jdbcTgChatRepository.add(2), urlId));

        List<TrackingUrlsEntity> trackingUrlsRepositoryByUrlId = jdbcTrackingUrlsRepository.findByUrlId(urlId);
        assert trackingUrlsRepositoryByUrlId.size() == 2;
    }

    @Test
    @Rollback
    @Transactional
    void findByUrlId_shouldReturnEmptyListIfThisUrlIsNotTracking() {
        long urlId = jdbcUrlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        List<TrackingUrlsEntity> trackingUrlsRepositoryByUrlId = jdbcTrackingUrlsRepository.findByUrlId(urlId);
        assert trackingUrlsRepositoryByUrlId.isEmpty();
    }

}
