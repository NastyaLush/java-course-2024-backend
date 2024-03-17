package edu.java.scrapper.repository.jdbc;

import edu.java.repository.jdbc.JdbcTgChatRepository;
import edu.java.repository.jdbc.JdbcTrackingUrlsRepository;
import edu.java.repository.jdbc.JdbcUrlRepository;
import edu.java.scrapper.repository.repository.TrackingJdbcUrlRepositoryTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JdbcTrackingUrlsRepositoryTest extends TrackingJdbcUrlRepositoryTest {

    protected JdbcTrackingUrlsRepositoryTest(
        JdbcTrackingUrlsRepository trackingUrlsRepository,
        JdbcTgChatRepository jdbcTgChatRepository,
        JdbcUrlRepository jdbcUrlRepository
    ) {
        super(trackingUrlsRepository, jdbcTgChatRepository, jdbcUrlRepository);
    }
}
