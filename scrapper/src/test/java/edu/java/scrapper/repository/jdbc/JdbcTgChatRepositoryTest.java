package edu.java.scrapper.repository.jdbc;

import edu.java.repository.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.repository.repository.TgChatRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest()
public class JdbcTgChatRepositoryTest extends TgChatRepositoryTest {

    @Autowired
    public JdbcTgChatRepositoryTest(JdbcTgChatRepository tgChatRepository) {
        super(tgChatRepository);
    }


}
