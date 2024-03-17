package edu.java.scrapper.repository.jooq;

import edu.java.repository.jooq.JooqUrlRepository;
import edu.java.scrapper.repository.repository.UrlRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JooqUrlRepositoryTest extends UrlRepositoryTest {
    @Autowired
    protected JooqUrlRepositoryTest(JooqUrlRepository urlRepository) {
        super(urlRepository);
    }
}
