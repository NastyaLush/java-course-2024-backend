package edu.java.scrapper.service;

import edu.java.ScrapperApplication;
import edu.java.exception.NotExistException;
import edu.java.model.LinkResponse;
import edu.java.repository.jpa.JpaUrlRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.TgChatService;
import edu.java.service.UrlService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ScrapperApplication.class,
        properties = {"app.database-access-type=jpa"})
public class JpaUrlServiceTest extends IntegrationTest {
    public static final long TG_CHAT_ID = 1L;
    public static final long TG_CHAT_ID_2 = 2L;
    public static final URI CORRECT_UTL = URI.create("https://stackoverflow.com/questions/10335747/how-to-download-xcode-dmg-or-xip-file/10335943#10335943");
    public static final URI CORRECT_UTL_2 = URI.create("https://stackoverflow.com/questions/1658707/how-to-download-xcode-iphone-os-entire-reference-library?rq=3");
    public static final URI CORRECT_UTL_3 = URI.create("https://stackoverflow.com/questions/5366794/xcode-4-downloads-mac-os-x-10-6-core-library?rq=3");
    public static final URI CORRECT_UTL_4 = URI.create("https://stackoverflow.com/questions/6733203/xcode-4-download?rq=3");
    public static final URI INCORRECT_UTL = URI.create("url");
    @Autowired
    UrlService urlService;
    @Autowired
    TgChatService tgChatService;
    @Autowired
    JpaUrlRepository jpaUrlRepository;


    @Transactional
    @Rollback
    @Test
    public void add_shouldCorrectlyAddUrlToChat() {
        tgChatService.register(TG_CHAT_ID);
        LinkResponse linkResponse = urlService.add(TG_CHAT_ID, CORRECT_UTL);
        assertTrue(urlService.getChats(linkResponse.getId())
                             .stream()
                             .map(chatEntity -> chatEntity.getTgChatId())
                             .toList()
                             .contains(TG_CHAT_ID));
    }

    @Transactional
    @Rollback
    @Test
    public void add_shouldDoNothingIfUrlAlreadyTracking() {
        tgChatService.register(TG_CHAT_ID);
        urlService.add(TG_CHAT_ID, CORRECT_UTL);
        assertDoesNotThrow(() -> urlService.add(TG_CHAT_ID, CORRECT_UTL));
    }

    @Transactional
    @Rollback
    @Test
    public void add_shouldThrowExceptionIfUrlWrong() {
        tgChatService.register(TG_CHAT_ID);
        assertThrows(IllegalArgumentException.class, () -> urlService.add(TG_CHAT_ID, INCORRECT_UTL));
    }

    @Transactional
    @Rollback
    @Test
    public void add_shouldThrowExceptionIfChatNotExist() {
        assertThrows(NotExistException.class, () -> urlService.add(TG_CHAT_ID, CORRECT_UTL));
    }

    @Transactional
    @Rollback
    @Test
    public void removeTracking_shouldCorrectlyUntrackUrl() {
        tgChatService.register(TG_CHAT_ID);
        urlService.add(TG_CHAT_ID, CORRECT_UTL);
        urlService.remove(TG_CHAT_ID, CORRECT_UTL);
        assertFalse(urlService.listAll(TG_CHAT_ID).getLinks()
                              .stream()
                              .map(LinkResponse::getUrl)
                              .toList()
                              .contains(CORRECT_UTL));
    }

    @Transactional
    @Rollback
    @Test
    public void removeTracking_shouldThrowExceptionIfUrlNotTracking() {
        tgChatService.register(TG_CHAT_ID);
        assertThrows(NotExistException.class, () -> urlService.remove(TG_CHAT_ID, CORRECT_UTL));
    }

    @Transactional
    @Rollback
    @Test
    public void removeTracking_shouldThrowExceptionIfChatNotExist() {
        assertThrows(NotExistException.class, () -> urlService.remove(TG_CHAT_ID, CORRECT_UTL));
    }

    @Transactional
    @Rollback
    @Test
    public void removeTracking_shouldRemoveUrlIfNobodyTrackIt() {
        tgChatService.register(TG_CHAT_ID);
        LinkResponse linkResponse = urlService.add(TG_CHAT_ID, CORRECT_UTL);
        urlService.remove(TG_CHAT_ID, CORRECT_UTL);
        assertThrows(NotExistException.class, () -> urlService.getChats(linkResponse.getId()));
    }

    @Transactional
    @Rollback
    @Test
    public void remove_shouldCorrectlyRemoveUrl() {
        tgChatService.register(TG_CHAT_ID);
        System.out.println(urlService.listAll(TG_CHAT_ID));
        urlService.add(TG_CHAT_ID, CORRECT_UTL);
        System.out.println(urlService.listAll(TG_CHAT_ID));
        urlService.remove(CORRECT_UTL);
        System.out.println(urlService.listAll(TG_CHAT_ID));
        assertFalse(urlService.listAll(TG_CHAT_ID).getLinks()
                              .stream()
                              .map(LinkResponse::getUrl)
                              .toList()
                              .contains(CORRECT_UTL));
    }

    @Transactional
    @Rollback
    @Test
    public void remove_shouldThrowExceptionIfUrlNotExist() {
        assertThrows(NotExistException.class, () -> urlService.remove(CORRECT_UTL));
    }

    @Transactional
    @Rollback
    @Test
    public void update_shouldCorrectlyUpdateUrl() {
        tgChatService.register(TG_CHAT_ID);
        LinkResponse linkResponse = urlService.add(TG_CHAT_ID, CORRECT_UTL);
        OffsetDateTime lastCheck = OffsetDateTime.now();
        urlService.update(linkResponse.getId(), lastCheck);
        assertTrue(jpaUrlRepository.findByUrl(String.valueOf(CORRECT_UTL))
                                   .get()
                                   .getLastCheck()
                                   .isEqual(lastCheck));
    }

    @Transactional
    @Rollback
    @Test
    public void update_shouldThrowExceptionIfUrlNotExist() {
        tgChatService.register(TG_CHAT_ID);
        OffsetDateTime lastCheck = OffsetDateTime.now();
        assertThrows(NotExistException.class, () -> urlService.update(1l, lastCheck));
    }

    @Transactional
    @Rollback
    @Test
    public void updateWithLastUpdate_shouldCorrectlyUpdateUrl() {
        tgChatService.register(TG_CHAT_ID);
        LinkResponse linkResponse = urlService.add(TG_CHAT_ID, CORRECT_UTL);
        OffsetDateTime lastCheck = OffsetDateTime.now();
        OffsetDateTime lastUpdate = OffsetDateTime.now();
        urlService.update(linkResponse.getId(), lastCheck, lastUpdate);
        assertTrue(jpaUrlRepository.findByUrl(String.valueOf(CORRECT_UTL))
                                   .get()
                                   .getLastCheck()
                                   .isEqual(lastCheck));
        assertTrue(jpaUrlRepository.findByUrl(String.valueOf(CORRECT_UTL))
                                   .get()
                                   .getLastUpdate()
                                   .isEqual(lastUpdate));
    }

    @Transactional
    @Rollback
    @Test
    public void updateWithLastUpdate_shouldThrowExceptionIfUrlNotExist() {
        tgChatService.register(TG_CHAT_ID);
        OffsetDateTime lastCheck = OffsetDateTime.now();
        OffsetDateTime lastUpdate = OffsetDateTime.now();
        assertThrows(NotExistException.class, () -> urlService.update(1l, lastCheck, lastUpdate));
    }

    @Transactional
    @Rollback
    @Test
    public void getChats_shouldCorrectlyReturnChats() {
        tgChatService.register(TG_CHAT_ID);
        tgChatService.register(TG_CHAT_ID_2);
        LinkResponse linkResponse = urlService.add(TG_CHAT_ID, CORRECT_UTL);
        urlService.add(TG_CHAT_ID_2, CORRECT_UTL);
        List<Long> expectedListTgChat = List.of(TG_CHAT_ID, TG_CHAT_ID_2);
        assertArrayEquals(urlService.getChats(linkResponse.getId())
                                    .stream()
                                    .map(chatEntity -> chatEntity.getTgChatId()).sorted()
                                    .toArray(), expectedListTgChat.stream().sorted().toArray());
    }

    @Transactional
    @Rollback
    @Test
    public void getChats_shouldThrowExceptionIfUrlNotExist() {
        tgChatService.register(TG_CHAT_ID);
        tgChatService.register(TG_CHAT_ID_2);
        assertThrows(NotExistException.class, () -> urlService.getChats(1l));
    }

    @Transactional
    @Rollback
    @Test
    public void listAll_shouldCorrectlyReturnTrackingUrls() {
        tgChatService.register(TG_CHAT_ID);
        urlService.add(TG_CHAT_ID, CORRECT_UTL);
        urlService.add(TG_CHAT_ID, CORRECT_UTL_2);
        List<URI> expectedUrls = List.of(CORRECT_UTL, CORRECT_UTL_2);
        assertArrayEquals(expectedUrls.stream()
                                      .sorted()
                                      .toArray(), urlService.listAll(TG_CHAT_ID)
                                                            .getLinks()
                                                            .stream()
                                                            .map(linkResponse -> linkResponse.getUrl())
                                                            .sorted()
                                                            .toArray()
        );
    }

    @Transactional
    @Rollback
    @Test
    public void listAll_shouldThrowExceptionIfChatNotExist() {
        assertThrows(NotExistException.class, () -> urlService.listAll(TG_CHAT_ID)
                                                              .getLinks()
                                                              .stream()
                                                              .map(linkResponse -> linkResponse.getUrl())
                                                              .toArray()
        );
    }

    @Transactional
    @Rollback
    @Test
    public void findNotCheckedForLongTime_shouldReturnNotCheckedForLongTimeUrls() {
        tgChatService.register(TG_CHAT_ID);
        LinkResponse add = urlService.add(TG_CHAT_ID, CORRECT_UTL);
        LinkResponse add1 = urlService.add(TG_CHAT_ID, CORRECT_UTL_2);
        LinkResponse add2 = urlService.add(TG_CHAT_ID, CORRECT_UTL_3);
        LinkResponse add3 = urlService.add(TG_CHAT_ID, CORRECT_UTL_4);
        urlService.update(add.getId(), OffsetDateTime.now()
                                                     .minusDays(1));
        urlService.update(add1.getId(), OffsetDateTime.now()
                                                      .minusDays(2));
        urlService.update(add2.getId(), OffsetDateTime.now()
                                                      .minusDays(3));
        urlService.update(add3.getId(), OffsetDateTime.now()
                                                      .minusDays(4));
        List<String> expectedUrls = List.of(CORRECT_UTL_2.toString(), CORRECT_UTL_3.toString(), CORRECT_UTL_4.toString());
        assertArrayEquals(
                expectedUrls.toArray(),
                urlService.findNotCheckedForLongTime(OffsetDateTime.now()
                                                                   .minusDays(2))
                          .stream()
                          .map(urlEntity -> urlEntity.getUrl())
                          .toArray()
        );
    }

    @Transactional
    @Rollback
    @Test
    public void findNotCheckedForLongTime_shouldCorrectlyWorkWithEmptyArray() {
        tgChatService.register(TG_CHAT_ID);

        List<String> expectedUrls = List.of();
        assertArrayEquals(
                expectedUrls.toArray(),
                urlService.findNotCheckedForLongTime(OffsetDateTime.now()
                                                                   .minusDays(2))
                          .stream()
                          .map(urlEntity -> urlEntity.getUrl())
                          .toArray()
        );
    }

}
