package edu.java.scrapper.repository.repository;


import edu.java.entity.TrackingUrlsDelete;
import edu.java.entity.TrackingUrlsEntity;
import edu.java.entity.TrackingUrlsInput;
import edu.java.entity.UrlInput;
import edu.java.repository.TgChatRepository;
import edu.java.repository.TrackingUrlsRepository;
import edu.java.repository.UrlRepository;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public abstract class TrackingJdbcUrlRepositoryTest extends IntegrationTest {

    private final TrackingUrlsRepository trackingUrlsRepository;

    private final TgChatRepository tgChatRepository;

    private final UrlRepository urlRepository;

    protected TrackingJdbcUrlRepositoryTest(
        TrackingUrlsRepository trackingUrlsRepository,
        TgChatRepository tgChatRepository,
        UrlRepository urlRepository
    ) {
        this.trackingUrlsRepository = trackingUrlsRepository;
        this.tgChatRepository = tgChatRepository;
        this.urlRepository = urlRepository;
    }

    @Test
    @Transactional
    @Rollback
    void add_shouldCorrectlyAddTrackingUrl() {
        long chatKey = tgChatRepository.add(1);
        long key =
            urlRepository.add(new UrlInput("https://www.google.com", OffsetDateTime.now(), OffsetDateTime.now()));
        trackingUrlsRepository.add(new TrackingUrlsInput(chatKey, key));
        assert trackingUrlsRepository.findAll().size() == 1;
    }

    @Test
    @Transactional
    @Rollback
    void remove_shouldCorrectlyRemoveTrackingUrl() {
        long chatKey = tgChatRepository.add(1);
        long key =
            urlRepository.add(new UrlInput("https://www.google.com", OffsetDateTime.now(), OffsetDateTime.now()));
        trackingUrlsRepository.add(new TrackingUrlsInput(chatKey, key));
        trackingUrlsRepository.remove(new TrackingUrlsDelete(chatKey, key));
        assert trackingUrlsRepository.findAll().isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findAll_shouldCorrectlyFindAllTrackingUrls() {
        long chatKey1 = tgChatRepository.add(1);
        long chatKey2 = tgChatRepository.add(2);
        long key =
            urlRepository.add(new UrlInput("https://www.google.com", OffsetDateTime.now(), OffsetDateTime.now()));
        trackingUrlsRepository.add(new TrackingUrlsInput(chatKey1, key));
        trackingUrlsRepository.add(new TrackingUrlsInput(chatKey2, key));
        assert trackingUrlsRepository.findAll().size() == 2;
    }

    @Test
    @Rollback
    @Transactional
    void findAll_shouldReturnEmptyListIfNoTrackingUrls() {
        assert trackingUrlsRepository.findAll().isEmpty();
    }

    @Test
    @Rollback
    @Transactional
    void findByChatId_shouldCorrectlyReturnValuesWithThisChatId() {
        long chatId = tgChatRepository.add(1);
        trackingUrlsRepository.add(new TrackingUrlsInput(
            chatId,
            urlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()))
        ));
        trackingUrlsRepository.add(new TrackingUrlsInput(
            chatId,
            urlRepository.add(new UrlInput("url2", OffsetDateTime.now(), OffsetDateTime.now()))
        ));
        trackingUrlsRepository.add(new TrackingUrlsInput(
            chatId,
            urlRepository.add(new UrlInput("url3", OffsetDateTime.now(), OffsetDateTime.now()))
        ));
        List<TrackingUrlsEntity> urlsRepositoryByChatId = trackingUrlsRepository.findByChatId(chatId);
        assert urlsRepositoryByChatId.size() == 3;
    }

    @Test
    @Rollback
    @Transactional
    void findByChatId_shouldReturnEmptyListIfNoTrackingUrls() {
        long chatId = tgChatRepository.add(1);
        List<TrackingUrlsEntity> urlsRepositoryByChatId = trackingUrlsRepository.findByChatId(chatId);
        assert urlsRepositoryByChatId.isEmpty();
    }

    @Test
    @Rollback
    @Transactional
    void findByUrlId_shouldCorrectlyReturnValuesWithThisUrlId() {
        long urlId = urlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        trackingUrlsRepository.add(new TrackingUrlsInput(tgChatRepository.add(1), urlId));
        trackingUrlsRepository.add(new TrackingUrlsInput(tgChatRepository.add(2), urlId));

        List<TrackingUrlsEntity> trackingUrlsRepositoryByUrlId = trackingUrlsRepository.findByUrlId(urlId);
        assert trackingUrlsRepositoryByUrlId.size() == 2;
    }

    @Test
    @Rollback
    @Transactional
    void findByUrlId_shouldReturnEmptyListIfThisUrlIsNotTracking() {
        long urlId = urlRepository.add(new UrlInput("url", OffsetDateTime.now(), OffsetDateTime.now()));
        List<TrackingUrlsEntity> trackingUrlsRepositoryByUrlId = trackingUrlsRepository.findByUrlId(urlId);
        assert trackingUrlsRepositoryByUrlId.isEmpty();
    }

}
