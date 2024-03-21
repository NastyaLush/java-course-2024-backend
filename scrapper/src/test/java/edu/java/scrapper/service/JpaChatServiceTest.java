package edu.java.scrapper.service;

import edu.java.ScrapperApplication;
import edu.java.entity.ChatEntity;
import edu.java.exception.AlreadyExistException;
import edu.java.exception.NotExistException;
import edu.java.repository.jpa.JpaTgChatRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.TgChatService;
import edu.java.service.jpa.JpaChatService;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ScrapperApplication.class,
        properties = {"app.database-access-type=jpa"})
public class JpaChatServiceTest extends IntegrationTest {
    public static final long TG_CHAT_ID = 1L;
    @Autowired
    TgChatService tgChatService;
    @Autowired
    JpaTgChatRepository jpaTgChatRepository;


    @Transactional
    @Rollback
    @Test
    public void register_shouldCorrectlyRegisterUser() {
        tgChatService.register(TG_CHAT_ID);
        assertTrue(jpaTgChatRepository.existsByTgChatId(TG_CHAT_ID));
    }
    @Transactional
    @Rollback
    @Test
    public void register_shouldThrowErrorIfUserAlreadyRegistered() {
        tgChatService.register(TG_CHAT_ID);
        assertThrows(AlreadyExistException.class, ()->tgChatService.register(TG_CHAT_ID));
    }
    @Transactional
    @Rollback
    @Test
    public void unregister_shouldCorrectlyRegisterUser() {
        tgChatService.register(TG_CHAT_ID);
        assertTrue(jpaTgChatRepository.existsByTgChatId(TG_CHAT_ID));
        tgChatService.unregister(TG_CHAT_ID);
        assertFalse(jpaTgChatRepository.existsByTgChatId(TG_CHAT_ID));
    }
    @Transactional
    @Rollback
    @Test
    public void unregister_shouldThrowErrorIfUserNotExist() {
        assertThrows(NotExistException.class, ()->tgChatService.unregister(TG_CHAT_ID));
    }
}
