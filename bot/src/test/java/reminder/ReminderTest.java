package reminder;

import edu.java.bot.db.ChatDB;
import edu.java.bot.db.DB;
import edu.java.bot.reminder.Handler;
import edu.java.bot.reminder.Reminder;
import edu.java.bot.tgBot.BotOperations;
import java.net.MalformedURLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

class ReminderTest {

    @Mock
    private DB db;

    @Mock
    private BotOperations botOperations;

    @Mock
    private Handler handler;

    @InjectMocks
    private Reminder reminder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void deleteUrl_shouldDeleteUrlFromDB() {
        Long chatId = 123L;
        URL url = mock(URL.class);

        reminder.deleteUrl(chatId, url);

        verify(db).deleteUrl(chatId, url);
    }

    @Test
    void getUrls_shouldReturnUserUrlsFromDB() throws MalformedURLException {
        Long chatId = 123L;
        List<URL> expectedUrls = Arrays.asList(new URL("http://example.com"), new URL("http://example.org"));

        when(db.getUrlsByPerson(chatId)).thenReturn(expectedUrls);

        List<URL> actualUrls = reminder.getUrls(chatId);

        assertEquals(expectedUrls, actualUrls);
    }

    @Test
    void createUser_shouldCallCreateMethodInDB() {
        ChatDB chatDB = mock(ChatDB.class);

        reminder.createUser(chatDB);

        verify(db).createUser(chatDB);
    }
}
