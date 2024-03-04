package edu.java.scrapper.repository;

import edu.java.repository.jdbc.JdbcTgChatRepository;
import edu.java.repository.dto.ChatDTO;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest()
public class JdbcTgChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcTgChatRepository jdbcTgChatRepository;

    @Test
    @Rollback
    @Transactional
    void add_shouldCorrectlyAddChat() {
        jdbcTgChatRepository.add(1);
        List<ChatDTO> chats = jdbcTgChatRepository.findAll();
        assert chats.size() == 1;
        assert chats.getFirst().chatId() == 1;
    }

    @Test
    @Rollback
    @Transactional
    void remove_shouldCorrectlyRemoveChat() {
        jdbcTgChatRepository.add(1);
        jdbcTgChatRepository.remove(1);
        List<ChatDTO> chats = jdbcTgChatRepository.findAll();
        assert chats.isEmpty();
    }

    @Test
    @Transactional
    void add_shouldThrowExceptionIfAddedSameChat() {
        jdbcTgChatRepository.add(1);
        assertThrows(DuplicateKeyException.class, () -> jdbcTgChatRepository.add(1));
    }


    @Test
    @Transactional
    void remove_shouldWorkCorrectlyIfRemovedChatDoesNotExist() {
        jdbcTgChatRepository.remove(1);
    }

    @Test
    @Transactional
    void findAll_shouldCorrectlyFindAllChats() {
        jdbcTgChatRepository.add(1);
        jdbcTgChatRepository.add(2);
        List<ChatDTO> chats = jdbcTgChatRepository.findAll();
        assert chats.size() == 2;
        assert chats.getFirst().chatId() == 1;
        assert chats.getLast().chatId() == 2;
    }

    @Test
    @Transactional
    void findAll_shouldReturnEmptyListIfNoChats() {
        List<ChatDTO> chats = jdbcTgChatRepository.findAll();
        assert chats.isEmpty();
    }
}
