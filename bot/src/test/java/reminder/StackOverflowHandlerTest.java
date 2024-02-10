package reminder;

import edu.java.bot.reminder.handlerImpl.StackOverflowHandler;
import java.net.URLConnection;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StackOverflowHandlerTest {

    @Test
    void handle_shouldReturnZonedDateTimeIfValidStackOverflowUrl() throws IOException {
        StackOverflowHandler stackOverflowHandler = new StackOverflowHandler();
        URL mockUrl = new URL("https://stackoverflow.com");
        URLConnection urlConnection = mockUrl.openConnection();
        long expected = urlConnection.getLastModified();

        ZonedDateTime actual = stackOverflowHandler.handle(mockUrl);

        assertEquals(ZonedDateTime.ofInstant(Instant.ofEpochMilli(expected), ZoneId.of("GMT")), actual);
    }

    @Test
    void handle_shouldThrowIllegalArgumentExceptionIfInvalidUrl() throws IOException {
        StackOverflowHandler stackOverflowHandler = new StackOverflowHandler();
        URL mockUrl = new URL("https://invalidurl.com");

        assertThrows(IllegalArgumentException.class, () -> stackOverflowHandler.handle(mockUrl));
    }

    @Test
    void canHandle_shouldReturnTrueValidStackOverflowUrl() throws IOException {
        StackOverflowHandler stackOverflowHandler = new StackOverflowHandler();
        URL mockUrl = new URL("https://stackoverflow.com");

        boolean result = stackOverflowHandler.canHandle(mockUrl);

        assertTrue(result);
    }

    @Test
    void canHandle_shouldReturnFalseIfInvalidUrl() throws IOException {
        StackOverflowHandler stackOverflowHandler = new StackOverflowHandler();
        URL mockUrl = new URL("https://invalidurl.com");

        boolean result = stackOverflowHandler.canHandle(mockUrl);

        assertFalse(result);
    }
}

