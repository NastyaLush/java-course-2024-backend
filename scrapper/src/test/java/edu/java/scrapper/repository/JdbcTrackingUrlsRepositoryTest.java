package edu.java.scrapper.repository;

import edu.java.repository.entity.TrackingUrlsDelete;
import edu.java.repository.jdbc.JdbcTgChatRepository;
import edu.java.repository.jdbc.JdbcTrackingUrlsRepository;
import edu.java.repository.jdbc.JdbcUrlRepository;
import edu.java.repository.entity.TrackingUrlsInput;
import edu.java.repository.entity.UrlInput;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
        long key = jdbcUrlRepository.add(new UrlInput("https://www.google.com",  OffsetDateTime.now(), OffsetDateTime.now()));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInput(chatKey,key));
        assert jdbcTrackingUrlsRepository.findAll().size() == 1;
    }

    @Test
    @Transactional
    @Rollback
    void remove_shouldCorrectlyRemoveTrackingUrl() {
        long chatKey = jdbcTgChatRepository.add(1);
        long key = jdbcUrlRepository.add(new UrlInput("https://www.google.com", OffsetDateTime.now(), OffsetDateTime.now()));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInput(chatKey,key));
        jdbcTrackingUrlsRepository.remove(new TrackingUrlsDelete(chatKey,key));
        assert jdbcTrackingUrlsRepository.findAll().isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findAll_shouldCorrectlyFindAllTrackingUrls() {
        long chatKey1 = jdbcTgChatRepository.add(1);
        long chatKey2 =jdbcTgChatRepository.add(2);
        long key = jdbcUrlRepository.add(new UrlInput("https://www.google.com", OffsetDateTime.now(), OffsetDateTime.now()));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInput(chatKey1,key));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInput(chatKey2,key));
        assert jdbcTrackingUrlsRepository.findAll().size() == 2;
    }

    @Test
    @Transactional
    @Rollback
    void remove_shouldWorkCorrectlyIfRemovedTrackingUrlDoesNotExist() {
        jdbcTrackingUrlsRepository.remove(new TrackingUrlsDelete(1l,1l));
    }

    @Test
    @Transactional
    void add_shouldThrowExceptionIfAddedSameTrackingUrl() {
        long chatKey = jdbcTgChatRepository.add(1);
        long key = jdbcUrlRepository.add(new UrlInput("https://www.google.com", OffsetDateTime.now(), OffsetDateTime.now()));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInput(chatKey,key));
        assertThrows(DuplicateKeyException.class, () -> jdbcTrackingUrlsRepository.add(new TrackingUrlsInput(chatKey,key)));
    }

    @Test
    @Transactional
    void findAll_shouldReturnEmptyListIfNoTrackingUrls() {
        assert jdbcTrackingUrlsRepository.findAll().isEmpty();
    }

}
