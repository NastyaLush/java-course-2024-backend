package edu.java.bot.url;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.Assert.assertEquals;

public class UrlServiceTest {
    private static Stream<Arguments> provideUrls() {
        return Stream.of(
            Arguments.of("", false),
            Arguments.of("u", false),
            Arguments.of("://jsj", false),
            Arguments.of("k://jsj", false),
            Arguments.of("https://www.baeldung.com/parameterized-tests-junit", true),
            Arguments.of("ftp://example.com", true),
            Arguments.of("http://invalid-url", true),
            Arguments.of("https://www.example.com", true),
            Arguments.of("https://subdomain.example.com", true),
            Arguments.of("http://localhost:8080", true),
            Arguments.of("https://www.invalid-domain@.com", true));
    }

    @ParameterizedTest
    @MethodSource("provideUrls")
    public void isUrl_shouldReturnTrueIfUrlCorrectAndFalseInAnotherCase(String givenString, boolean expectedAnswer){
        UrlService urlService = new UrlService();

        boolean actualAnswer = urlService.isUrl(givenString);

        assertEquals(expectedAnswer,actualAnswer);
    }
}
