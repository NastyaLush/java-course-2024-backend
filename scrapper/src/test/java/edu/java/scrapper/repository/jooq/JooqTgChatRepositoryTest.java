package edu.java.scrapper.repository.jooq;

import edu.java.repository.entity.ChatEntity;
import edu.java.repository.jooq.JooqTgChatRepository;
import java.util.List;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
@SpringBootTest()
public class JooqTgChatRepositoryTest extends IntegrationTest {
    private final JooqTgChatRepository jooqTgChatRepository;

    @Autowired
    public JooqTgChatRepositoryTest(JooqTgChatRepository jooqTgChatRepository) {
        this.jooqTgChatRepository = jooqTgChatRepository;
    }

    @Test
    @Rollback
    @Transactional
    void add_shouldCorrectlyAddChat() {
        jooqTgChatRepository.add(1l);
        List<ChatEntity> chats = jooqTgChatRepository.findAll();
        assert chats.size() == 1;
        assert chats.getFirst().tgChatId() == 1;
    }
}
