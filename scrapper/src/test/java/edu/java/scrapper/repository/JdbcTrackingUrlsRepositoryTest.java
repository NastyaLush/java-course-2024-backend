package edu.java.scrapper.repository;

import edu.java.repository.jdbc.JdbcTgChatRepository;
import edu.java.repository.jdbc.JdbcTrackingUrlsRepository;
import edu.java.repository.jdbc.JdbcUrlRepository;
import edu.java.repository.dto.TrackingUrlsInputDTO;
import edu.java.repository.dto.UrlInputDTO;
import edu.java.scrapper.IntegrationTest;
import java.time.ZonedDateTime;
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
        int chatKey = jdbcTgChatRepository.add(1);
        int key = jdbcUrlRepository.add(new UrlInputDTO("https://www.google.com", ZonedDateTime.now(), ZonedDateTime.now()));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInputDTO(chatKey,key));
        assert jdbcTrackingUrlsRepository.findAll().size() == 1;
    }

    @Test
    @Transactional
    @Rollback
    void remove_shouldCorrectlyRemoveTrackingUrl() {
        int chatKey = jdbcTgChatRepository.add(1);
        int key = jdbcUrlRepository.add(new UrlInputDTO("https://www.google.com", ZonedDateTime.now(), ZonedDateTime.now()));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInputDTO(chatKey,key));
        jdbcTrackingUrlsRepository.remove(new TrackingUrlsInputDTO(chatKey,key));
        assert jdbcTrackingUrlsRepository.findAll().isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findAll_shouldCorrectlyFindAllTrackingUrls() {
        int chatKey1 = jdbcTgChatRepository.add(1);
        int chatKey2 =jdbcTgChatRepository.add(2);
        int key = jdbcUrlRepository.add(new UrlInputDTO("https://www.google.com", ZonedDateTime.now(), ZonedDateTime.now()));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInputDTO(chatKey1,key));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInputDTO(chatKey2,key));
        assert jdbcTrackingUrlsRepository.findAll().size() == 2;
    }

    @Test
    @Transactional
    @Rollback
    void remove_shouldWorkCorrectlyIfRemovedTrackingUrlDoesNotExist() {
        jdbcTrackingUrlsRepository.remove(new TrackingUrlsInputDTO(1,1));
    }

    @Test
    @Transactional
    void add_shouldThrowExceptionIfAddedSameTrackingUrl() {
        int chatKey = jdbcTgChatRepository.add(1);
        int key = jdbcUrlRepository.add(new UrlInputDTO("https://www.google.com", ZonedDateTime.now(), ZonedDateTime.now()));
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInputDTO(chatKey,key));
        assertThrows(DuplicateKeyException.class, () -> jdbcTrackingUrlsRepository.add(new TrackingUrlsInputDTO(chatKey,key)));
    }

    @Test
    @Transactional
    void findAll_shouldReturnEmptyListIfNoTrackingUrls() {
        assert jdbcTrackingUrlsRepository.findAll().isEmpty();
    }

}
