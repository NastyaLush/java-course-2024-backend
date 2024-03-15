package edu.java.scrapper.repository;

import edu.java.exception.NotExistException;
import edu.java.repository.entity.ChatEntity;
import edu.java.repository.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        List<ChatEntity> chats = jdbcTgChatRepository.findAll();
        assert chats.size() == 1;
        assert chats.getFirst().tgChatId() == 1;
    }

    @Test
    @Rollback
    @Transactional
    void remove_shouldCorrectlyRemoveChat() {
        jdbcTgChatRepository.add(1);
        jdbcTgChatRepository.remove(1);
        List<ChatEntity> chats = jdbcTgChatRepository.findAll();
        assert chats.isEmpty();
    }

    @Test
    @Rollback
    @Transactional
    void add_shouldThrowExceptionIfAddedSameChat() {
        jdbcTgChatRepository.add(1);
        assertThrows(IllegalArgumentException.class, () -> jdbcTgChatRepository.add(1));
    }

    @Test
    @Rollback
    @Transactional
    void remove_shouldThrowExceptionIfRemovedChatDoesNotExist() {
        assertThrows(NotExistException.class, () -> jdbcTgChatRepository.remove(1));
    }

    @Test
    @Rollback
    @Transactional
    void findAll_shouldCorrectlyFindAllChats() {
        jdbcTgChatRepository.add(1);
        jdbcTgChatRepository.add(2);
        List<ChatEntity> chats = jdbcTgChatRepository.findAll();
        assert chats.size() == 2;
        assert chats.getFirst().tgChatId() == 1;
        assert chats.getLast().tgChatId() == 2;
    }

    @Test
    @Rollback
    @Transactional
    void findAll_shouldReturnEmptyListIfNoChats() {
        List<ChatEntity> chats = jdbcTgChatRepository.findAll();
        assert chats.isEmpty();
    }

    @Test
    @Rollback
    @Transactional
    void findById_shouldCorrectlyFindChatById() {
        long id = jdbcTgChatRepository.add(1L);
        Optional<ChatEntity> chatEntity = jdbcTgChatRepository.findByTgId(1L);
        assert chatEntity.isPresent();
        assert chatEntity.get().id().equals(id);
    }

    @Test
    @Rollback
    @Transactional
    void findById_shouldReturnEmptyOptionalIfChatNotExist() {
        Optional<ChatEntity> chatEntity = jdbcTgChatRepository.findByTgId(1);
        assert chatEntity.isEmpty();
    }
}
