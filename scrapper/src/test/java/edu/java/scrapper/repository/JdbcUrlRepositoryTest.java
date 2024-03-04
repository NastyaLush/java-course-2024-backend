package edu.java.scrapper.repository;

import edu.java.repository.dto.UrlDTO;
import edu.java.repository.dto.UrlInputDTO;
import edu.java.repository.jdbc.JdbcUrlRepository;
import edu.java.scrapper.IntegrationTest;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
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
        jdbcUrlRepository.add(new UrlInputDTO("url", ZonedDateTime.now(), ZonedDateTime.now()));
        List<UrlDTO> urlDTOList = jdbcUrlRepository.findAll();
        assert urlDTOList.size() == 1;
        assert urlDTOList.getFirst().url().equals("url");
    }

    @Test
    @Transactional
    @Rollback
    void remove_shouldCorrectlyRemoveChat() {
        jdbcUrlRepository.add(new UrlInputDTO("url", ZonedDateTime.now(), ZonedDateTime.now()));
        jdbcUrlRepository.remove("url");
        List<UrlDTO> urlDTOList = jdbcUrlRepository.findAll();
        assert urlDTOList.isEmpty();
    }

    @Test
    @Transactional
    void remove_shouldWorkCorrectlyIfRemovedChatDoesNotExist() {
        jdbcUrlRepository.remove("url");
    }

    @Test
    @Transactional
    void findAll_shouldCorrectlyFindAllChats() {
        jdbcUrlRepository.add(new UrlInputDTO("url1", ZonedDateTime.now(), ZonedDateTime.now()));
        jdbcUrlRepository.add(new UrlInputDTO("url2", ZonedDateTime.now(), ZonedDateTime.now()));
        List<UrlDTO> urlDTOList = jdbcUrlRepository.findAll();
        assert urlDTOList.size() == 2;
    }

    @Test
    @Transactional
    void add_shouldReturnSameKeyIfAddedSameChat() {
        int key1 = jdbcUrlRepository.add(new UrlInputDTO("url", ZonedDateTime.now(), ZonedDateTime.now()));
        int key2 = jdbcUrlRepository.add(new UrlInputDTO("url", ZonedDateTime.now(), ZonedDateTime.now()));
        assert key1 == key2;
    }


    @Test
    @Transactional
    void findAll_shouldThrowExceptionIfNoChats() {
        List<UrlDTO> urlDTOList = jdbcUrlRepository.findAll();
        assert urlDTOList.isEmpty();
    }
}
