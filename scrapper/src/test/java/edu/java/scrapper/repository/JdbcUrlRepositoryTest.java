package edu.java.scrapper.repository;

import edu.java.exception.NotExistException;
import edu.java.entity.UrlEntity;
import edu.java.entity.UrlInput;
import edu.java.repository.jdbc.JdbcUrlRepository;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JdbcUrlRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcUrlRepository jdbcUrlRepository;

    @Test
    @Transactional
    @Rollback
    void add_shouldCorrectlyAddLink() {
        jdbcUrlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        List<UrlEntity> urlEntityList = jdbcUrlRepository.findAll();
        assert urlEntityList.size() == 1;
        assert urlEntityList.getFirst().url().equals("url");
    }

    @Test
    @Transactional
    @Rollback
    void remove_shouldCorrectlyRemoveLink() {
        jdbcUrlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        jdbcUrlRepository.remove("url");
        List<UrlEntity> urlEntityList = jdbcUrlRepository.findAll();
        assert urlEntityList.isEmpty();
    }

    @Test
    @Rollback
    @Transactional
    void remove_shouldThrowExceptionIfRemovedLinkDoesNotExist() {
        assertThrows(NotExistException.class,()->jdbcUrlRepository.remove("url"));
    }

    @Test
    @Rollback
    @Transactional
    void findAll_shouldCorrectlyFindAllChats() {
        jdbcUrlRepository.add(new UrlInput("url1", OffsetDateTime.now(), OffsetDateTime.now()));
        jdbcUrlRepository.add(new UrlInput("url2", OffsetDateTime.now(), OffsetDateTime.now()));
        List<UrlEntity> urlEntityList = jdbcUrlRepository.findAll();
        assert urlEntityList.size() == 2;
    }

    @Test
    @Rollback
    @Transactional
    void add_shouldReturnSameKeyIfAddedSameChat() {
        long key1 = jdbcUrlRepository.add(new UrlInput("url",  OffsetDateTime.now(), OffsetDateTime.now()));
        long key2 = jdbcUrlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        assert key1 == key2;
    }


    @Test
    @Rollback
    @Transactional
    void findAll_shouldThrowExceptionIfNoChats() {
        List<UrlEntity> urlEntityList = jdbcUrlRepository.findAll();
        assert urlEntityList.isEmpty();
    }

    @Test
    @Rollback
    @Transactional
    void findById_shouldCorrectlyFindUrltById() {
        long id = jdbcUrlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        Optional<UrlEntity> byId = jdbcUrlRepository.findById(id);
        assert byId.isPresent();
        assert byId.get().url().equals("url");
    }

    @Test
    @Rollback
    @Transactional
    void findById_shouldReturnEmptyOptionalIfChatNotExist() {
        Optional<UrlEntity> byId = jdbcUrlRepository.findById(1);
        assert byId.isEmpty();
    }
    @Test
    @Rollback
    @Transactional
    void findByUrl_shouldCorrectlyFindUrlByUrl() {
        long id = jdbcUrlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        Optional<UrlEntity> byUrl = jdbcUrlRepository.findByUrl("url");
        assert byUrl.isPresent();
        assert byUrl.get().id().equals(id);
    }

    @Test
    @Rollback
    @Transactional
    void findByUrl_shouldReturnEmptyOptionalIfChatNotExist() {
        Optional<UrlEntity> byId = jdbcUrlRepository.findByUrl("url");
        assert byId.isEmpty();
    }
    @Test
    @Rollback
    @Transactional
    void update_shouldCorrectlyUpdateUrl() {
        UrlInput url = new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now());
        long id = jdbcUrlRepository.add(url);
        OffsetDateTime lastCheck = OffsetDateTime.now();
        OffsetDateTime lastUpdate = OffsetDateTime.now();
        Optional<UrlEntity> before = jdbcUrlRepository.findById(id);
        jdbcUrlRepository.update(id, lastCheck, lastUpdate);
        Optional<UrlEntity> after = jdbcUrlRepository.findById(id);

        assert after.isPresent();
        assert after.get().id().equals(id);
        assert after.get().url().equals(url.url());
        assert !after.get().lastCheck().isEqual(before.get().lastCheck());
        assert !after.get().lastUpdate().isEqual(before.get().lastUpdate());
    }
    @Test
    @Rollback
    @Transactional
    void update_shouldThrowExceptionIfNoUrlWithThisId() {
        assertThrows(IllegalArgumentException.class,()->jdbcUrlRepository.update(1L, OffsetDateTime.now(), OffsetDateTime.now()));
    }
    @Test
    @Rollback
    @Transactional
    void updateLastCheck_shouldCorrectlyUpdateUrl() {
        UrlInput url = new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now());
        long id = jdbcUrlRepository.add(url);
        OffsetDateTime lastCheck = OffsetDateTime.now();
        Optional<UrlEntity> before = jdbcUrlRepository.findById(id);
        jdbcUrlRepository.update(id, lastCheck);
        Optional<UrlEntity> after = jdbcUrlRepository.findById(id);
        assert after.isPresent();
        assert after.get().id().equals(id);
        assert after.get().url().equals(url.url());
        assert !after.get().lastCheck().isEqual(before.get().lastCheck());
        assert  after.get().lastUpdate().isEqual(before.get().lastUpdate());
    }
    @Test
    @Rollback
    @Transactional
    void updateLastCheck_shouldThrowExceptionIfNoUrlWithThisId() {
        assertThrows(IllegalArgumentException.class,()->jdbcUrlRepository.update(1L, OffsetDateTime.now()));
    }
    @Test
    @Rollback
    @Transactional
    void findNotCheckedForLongTime_shouldCorrectlyReturnNotUpdatedForLongTimeUrls() {
        jdbcUrlRepository.add(new UrlInput("url1", OffsetDateTime.now(), OffsetDateTime.now().minusSeconds(10)));
        jdbcUrlRepository.add(new UrlInput("url2", OffsetDateTime.now(), OffsetDateTime.now().minusSeconds(20)));
        jdbcUrlRepository.add(new UrlInput("url3", OffsetDateTime.now(), OffsetDateTime.now().minusSeconds(30)));
        jdbcUrlRepository.add(new UrlInput("url4", OffsetDateTime.now(), OffsetDateTime.now().minusSeconds(40)));

        List<UrlEntity> notCheckedForLongTime =
            jdbcUrlRepository.findNotCheckedForLongTime(OffsetDateTime.now().minusSeconds(25));

        assert notCheckedForLongTime.size()==2;
        assert notCheckedForLongTime.stream().filter(urlEntity -> urlEntity.url().equals("url3")|| urlEntity.url().equals("url4")).count()==2;
    }
    @Test
    @Rollback
    @Transactional
    void findNotCheckedForLongTime_shouldCorrectlyWorkWithEmptyUrls() {

        List<UrlEntity> notCheckedForLongTime =
            jdbcUrlRepository.findNotCheckedForLongTime(OffsetDateTime.now().minusSeconds(25));

        assert notCheckedForLongTime.isEmpty();
    }
}
