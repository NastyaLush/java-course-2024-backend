package edu.java.scrapper.repository;

import edu.java.repository.entity.UrlEntity;
import edu.java.repository.entity.UrlInput;
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
public class JdbcUrlRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcUrlRepository jdbcUrlRepository;

    @Test
    @Transactional
    @Rollback
    void add_shouldCorrectlyAddChat() {
        jdbcUrlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        List<UrlEntity> urlEntityList = jdbcUrlRepository.findAll();
        assert urlEntityList.size() == 1;
        assert urlEntityList.getFirst().url().equals("url");
    }

    @Test
    @Transactional
    @Rollback
    void remove_shouldCorrectlyRemoveChat() {
        jdbcUrlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        jdbcUrlRepository.remove("url");
        List<UrlEntity> urlEntityList = jdbcUrlRepository.findAll();
        assert urlEntityList.isEmpty();
    }

    @Test
    @Transactional
    void remove_shouldWorkCorrectlyIfRemovedChatDoesNotExist() {
        jdbcUrlRepository.remove("url");
    }

    @Test
    @Transactional
    void findAll_shouldCorrectlyFindAllChats() {
        jdbcUrlRepository.add(new UrlInput("url1", OffsetDateTime.now(), OffsetDateTime.now()));
        jdbcUrlRepository.add(new UrlInput("url2", OffsetDateTime.now(), OffsetDateTime.now()));
        List<UrlEntity> urlEntityList = jdbcUrlRepository.findAll();
        assert urlEntityList.size() == 2;
    }

    @Test
    @Transactional
    void add_shouldReturnSameKeyIfAddedSameChat() {
        long key1 = jdbcUrlRepository.add(new UrlInput("url",  OffsetDateTime.now(), OffsetDateTime.now()));
        long key2 = jdbcUrlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        assert key1 == key2;
    }


    @Test
    @Transactional
    void findAll_shouldThrowExceptionIfNoChats() {
        List<UrlEntity> urlEntityList = jdbcUrlRepository.findAll();
        assert urlEntityList.isEmpty();
    }
}
