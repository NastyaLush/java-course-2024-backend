package edu.java.scrapper.repository.jooq;

import edu.java.repository.jooq.JooqTgChatRepository;
import edu.java.scrapper.repository.repository.TgChatRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JooqTgChatRepositoryTest extends TgChatRepositoryTest {

    @Autowired
    public JooqTgChatRepositoryTest(JooqTgChatRepository tgChatRepository) {
        super(tgChatRepository);
    }
}
