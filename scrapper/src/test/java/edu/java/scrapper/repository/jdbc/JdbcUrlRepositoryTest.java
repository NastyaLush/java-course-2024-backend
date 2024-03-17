package edu.java.scrapper.repository.jdbc;

import edu.java.repository.jdbc.JdbcUrlRepository;
import edu.java.scrapper.repository.repository.UrlRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JdbcUrlRepositoryTest extends UrlRepositoryTest {

    @Autowired
    protected JdbcUrlRepositoryTest(JdbcUrlRepository urlRepository) {
        super(urlRepository);
    }
}
