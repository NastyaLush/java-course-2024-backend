package edu.java.bot.url;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class UrlManagerTest {


    @ParameterizedTest
    @CsvSource({
            "https://www.baeldung.com/parameterized-tests-junit-5,www.baeldung.com",
            "https://example.com/path/to/page,example.com",
            "https://subdomain.example.org/index.html,subdomain.example.org",
            "https://localhost:8080/test/index.html,localhost",
            "https://192.168.1.1:3000/app/home,192.168.1.1",
            "ftp://ftp.example.com/file.zip,ftp.example.com",
            "http://test.site.org/page.html,test.site.org",
            "https://secure.server.com/data/info,secure.server.com",

    })
    public void getUrlDomainName_shouldReturnHost(String url, String expected) throws MalformedURLException {
        String actual = UrlManager.getUrlDomainName(new URL(url));

        assertEquals(expected,actual);
    }

}
