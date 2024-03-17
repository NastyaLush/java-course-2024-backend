package edu.java.scrapper.repository.repository;

import edu.java.exception.AlreadyExistException;
import edu.java.exception.NotExistException;
import edu.java.repository.entity.ChatEntity;
import edu.java.repository.interf.TgChatRepository;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class TgChatRepositoryTest extends IntegrationTest {
    private final TgChatRepository tgChatRepository;

    public TgChatRepositoryTest(TgChatRepository tgChatRepository) {
        this.tgChatRepository = tgChatRepository;
    }

    @Test
    @Rollback
    @Transactional
    void add_shouldCorrectlyAddChat() {
        tgChatRepository.add(1l);
        List<ChatEntity> chats = tgChatRepository.findAll();
        assert chats.size() == 1;
        assert chats.getFirst().tgChatId() == 1;
    }

    @Test
    @Rollback
    @Transactional
    void remove_shouldCorrectlyRemoveChat() {
        tgChatRepository.add(1);
        tgChatRepository.remove(1);
        List<ChatEntity> chats = tgChatRepository.findAll();
        assert chats.isEmpty();
    }

    @Test
    @Rollback
    @Transactional
    void add_shouldThrowExceptionIfAddedSameChat() {
        tgChatRepository.add(1);
        assertThrows(AlreadyExistException.class, () -> tgChatRepository.add(1));
    }

    @Test
    @Rollback
    @Transactional
    void remove_shouldThrowExceptionIfRemovedChatDoesNotExist() {
        assertThrows(NotExistException.class, () -> tgChatRepository.remove(1));
    }

    @Test
    @Rollback
    @Transactional
    void findAll_shouldCorrectlyFindAllChats() {
        tgChatRepository.add(1);
        tgChatRepository.add(2);
        List<ChatEntity> chats = tgChatRepository.findAll();
        assert chats.size() == 2;
        assert chats.getFirst().tgChatId() == 1;
        assert chats.getLast().tgChatId() == 2;
    }

    @Test
    @Rollback
    @Transactional
    void findAll_shouldReturnEmptyListIfNoChats() {
        List<ChatEntity> chats = tgChatRepository.findAll();
        assert chats.isEmpty();
    }

    @Test
    @Rollback
    @Transactional
    void findById_shouldCorrectlyFindChatById() {
        long id = tgChatRepository.add(1L);
        Optional<ChatEntity> chatEntity = tgChatRepository.findByTgId(1L);
        assert chatEntity.isPresent();
        assert chatEntity.get().id().equals(id);
    }

    @Test
    @Rollback
    @Transactional
    void findById_shouldReturnEmptyOptionalIfChatNotExist() {
        Optional<ChatEntity> chatEntity = tgChatRepository.findByTgId(1);
        assert chatEntity.isEmpty();
    }
}
