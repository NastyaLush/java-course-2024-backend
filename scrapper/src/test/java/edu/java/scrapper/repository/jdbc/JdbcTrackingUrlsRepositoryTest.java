package edu.java.scrapper.repository.jdbc;

import edu.java.repository.jdbc.JdbcTgChatRepository;
import edu.java.repository.jdbc.JdbcTrackingUrlsRepository;
import edu.java.repository.jdbc.JdbcUrlRepository;
import edu.java.scrapper.repository.repository.TrackingJdbcUrlRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JdbcTrackingUrlsRepositoryTest extends TrackingJdbcUrlRepositoryTest {
    @Autowired

    protected JdbcTrackingUrlsRepositoryTest(
        JdbcTrackingUrlsRepository trackingUrlsRepository,
        JdbcTgChatRepository jdbcTgChatRepository,
        JdbcUrlRepository jdbcUrlRepository
    ) {
        super(trackingUrlsRepository, jdbcTgChatRepository, jdbcUrlRepository);
    }
}
