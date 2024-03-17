package edu.java.scrapper.repository.jooq;

import edu.java.repository.jooq.JooqTgChatRepository;
import edu.java.repository.jooq.JooqTrackingUrlsRepository;
import edu.java.repository.jooq.JooqUrlRepository;
import edu.java.scrapper.repository.repository.TrackingJdbcUrlRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
public class JooqTrackingUrlRepositoryTest extends TrackingJdbcUrlRepositoryTest {
    @Autowired
    protected JooqTrackingUrlRepositoryTest(
        JooqTrackingUrlsRepository trackingUrlsRepository,
        JooqTgChatRepository tgChatRepository,
        JooqUrlRepository urlRepository
    ) {
        super(trackingUrlsRepository, tgChatRepository, urlRepository);
    }
}
