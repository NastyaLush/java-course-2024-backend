package edu.java.scrapper.repository.repository;

import edu.java.exception.NotExistException;
import edu.java.repository.entity.UrlEntity;
import edu.java.repository.entity.UrlInput;
import edu.java.repository.interf.UrlRepository;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class UrlRepositoryTest extends IntegrationTest {

    private final UrlRepository urlRepository;

    protected UrlRepositoryTest(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Test
    @Transactional
    @Rollback
    void add_shouldCorrectlyAddLink() {
        urlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        List<UrlEntity> urlEntityList = urlRepository.findAll();
        assert urlEntityList.size() == 1;
        assert urlEntityList.getFirst().url().equals("url");
    }

    @Test
    @Transactional
    @Rollback
    void remove_shouldCorrectlyRemoveLink() {
        urlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        urlRepository.remove("url");
        List<UrlEntity> urlEntityList = urlRepository.findAll();
        assert urlEntityList.isEmpty();
    }

    @Test
    @Rollback
    @Transactional
    void remove_shouldThrowExceptionIfRemovedLinkDoesNotExist() {
        assertThrows(NotExistException.class, () -> urlRepository.remove("url"));
    }

    @Test
    @Rollback
    @Transactional
    void findAll_shouldCorrectlyFindAllChats() {
        urlRepository.add(new UrlInput("url1", OffsetDateTime.now(), OffsetDateTime.now()));
        urlRepository.add(new UrlInput("url2", OffsetDateTime.now(), OffsetDateTime.now()));
        List<UrlEntity> urlEntityList = urlRepository.findAll();
        assert urlEntityList.size() == 2;
    }

    @Test
    @Rollback
    @Transactional
    void add_shouldReturnSameKeyIfAddedSameChat() {
        long key1 = urlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        long key2 = urlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        assert key1 == key2;
    }

    @Test
    @Rollback
    @Transactional
    void findAll_shouldThrowExceptionIfNoChats() {
        List<UrlEntity> urlEntityList = urlRepository.findAll();
        assert urlEntityList.isEmpty();
    }

    @Test
    @Rollback
    @Transactional
    void findById_shouldCorrectlyFindUrltById() {
        long id = urlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        Optional<UrlEntity> byId = urlRepository.findById(id);
        assert byId.isPresent();
        assert byId.get().url().equals("url");
    }

    @Test
    @Rollback
    @Transactional
    void findById_shouldReturnEmptyOptionalIfChatNotExist() {
        Optional<UrlEntity> byId = urlRepository.findById(1);
        assert byId.isEmpty();
    }

    @Test
    @Rollback
    @Transactional
    void findByUrl_shouldCorrectlyFindUrlByUrl() {
        long id = urlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        Optional<UrlEntity> byUrl = urlRepository.findByUrl("url");
        assert byUrl.isPresent();
        assert byUrl.get().id().equals(id);
    }

    @Test
    @Rollback
    @Transactional
    void findByUrl_shouldReturnEmptyOptionalIfChatNotExist() {
        Optional<UrlEntity> byId = urlRepository.findByUrl("url");
        assert byId.isEmpty();
    }

    @Test
    @Rollback
    @Transactional
    void update_shouldCorrectlyUpdateUrl() {
        UrlInput url = new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now());
        long id = urlRepository.add(url);
        OffsetDateTime lastCheck = OffsetDateTime.now();
        OffsetDateTime lastUpdate = OffsetDateTime.now();
        Optional<UrlEntity> before = urlRepository.findById(id);
        urlRepository.update(id, lastCheck, lastUpdate);
        Optional<UrlEntity> after = urlRepository.findById(id);

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
        assertThrows(
            NotExistException.class,
            () -> urlRepository.update(1L, OffsetDateTime.now(), OffsetDateTime.now())
        );
    }

    @Test
    @Rollback
    @Transactional
    void updateLastCheck_shouldCorrectlyUpdateUrl() {
        UrlInput url = new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now());
        long id = urlRepository.add(url);
        OffsetDateTime lastCheck = OffsetDateTime.now();
        Optional<UrlEntity> before = urlRepository.findById(id);
        urlRepository.update(id, lastCheck);
        Optional<UrlEntity> after = urlRepository.findById(id);
        assert after.isPresent();
        assert after.get().id().equals(id);
        assert after.get().url().equals(url.url());
        assert !after.get().lastCheck().isEqual(before.get().lastCheck());
        assert after.get().lastUpdate().isEqual(before.get().lastUpdate());
    }

    @Test
    @Rollback
    @Transactional
    void updateLastCheck_shouldThrowExceptionIfNoUrlWithThisId() {
        assertThrows(NotExistException.class, () -> urlRepository.update(1L, OffsetDateTime.now()));
    }

    @Test
    @Rollback
    @Transactional
    void findNotCheckedForLongTime_shouldCorrectlyReturnNotUpdatedForLongTimeUrls() {
        urlRepository.add(new UrlInput("url1", OffsetDateTime.now(), OffsetDateTime.now().minusSeconds(10)));
        urlRepository.add(new UrlInput("url2", OffsetDateTime.now(), OffsetDateTime.now().minusSeconds(20)));
        urlRepository.add(new UrlInput("url3", OffsetDateTime.now(), OffsetDateTime.now().minusSeconds(30)));
        urlRepository.add(new UrlInput("url4", OffsetDateTime.now(), OffsetDateTime.now().minusSeconds(40)));

        List<UrlEntity> notCheckedForLongTime =
            urlRepository.findNotCheckedForLongTime(OffsetDateTime.now().minusSeconds(25));

        assert notCheckedForLongTime.size() == 2;
        assert notCheckedForLongTime.stream()
                                    .filter(urlEntity -> urlEntity.url().equals("url3")
                                        || urlEntity.url().equals("url4"))
                                    .count() == 2;
    }

    @Test
    @Rollback
    @Transactional
    void findNotCheckedForLongTime_shouldCorrectlyWorkWithEmptyUrls() {

        List<UrlEntity> notCheckedForLongTime =
            urlRepository.findNotCheckedForLongTime(OffsetDateTime.now().minusSeconds(25));

        assert notCheckedForLongTime.isEmpty();
    }
}
