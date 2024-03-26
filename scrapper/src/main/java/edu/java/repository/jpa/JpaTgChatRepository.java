package edu.java.repository.jpa;

import edu.java.entity.ChatEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTgChatRepository extends JpaRepository<ChatEntity, Integer> {
    boolean existsByTgChatId(Long tgChatId);

    Optional<ChatEntity> findByTgChatId(Long tgChatId);

    void deleteByTgChatId(Long tgChatId);
}
