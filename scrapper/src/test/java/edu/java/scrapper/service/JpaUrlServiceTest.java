package edu.java.scrapper.service;

import edu.java.ScrapperApplication;
import edu.java.exception.AlreadyExistException;
import edu.java.model.LinkResponse;
import edu.java.repository.jpa.JpaUrlRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.TgChatService;
import edu.java.service.UrlService;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ScrapperApplication.class,
        properties = {"app.database-access-type=jpa"})
public class JpaUrlServiceTest extends IntegrationTest {
    public static final long TG_CHAT_ID = 1L;
    public static final URI CORRECT_UTL = URI.create("");
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
        assertTrue(urlService.getChats(linkResponse.getId()).stream().map(chatEntity -> chatEntity.getTgChatId()).toList().contains(TG_CHAT_ID));
    }
    @Transactional
    @Rollback
    @Test
    public void add_shouldThrowExceptionIfUrlAlreadyTracking() {
        tgChatService.register(TG_CHAT_ID);
        urlService.add(TG_CHAT_ID, CORRECT_UTL);
        assertThrows( AlreadyExistException.class, ()->urlService.add(TG_CHAT_ID, CORRECT_UTL));
    }
    @Transactional
    @Rollback
    @Test
    public void add_shouldThrowExceptionIfUrlWrong() {
        tgChatService.register(TG_CHAT_ID);
        assertThrows( IllegalArgumentException.class, ()->urlService.add(TG_CHAT_ID, INCORRECT_UTL));
   }
    @Transactional
    @Rollback
    @Test
    public void add_shouldThrowExceptionIfChatNotExist() {
        assertThrows( IllegalArgumentException.class, ()->urlService.add(TG_CHAT_ID, CORRECT_UTL));
    }
}
