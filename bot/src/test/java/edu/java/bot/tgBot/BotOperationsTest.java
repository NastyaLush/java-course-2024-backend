package edu.java.bot.tgBot;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.db.ChatDB;
import edu.java.bot.tgBot.BotOperations;
import edu.java.bot.tgBot.TelegramBotImpl;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BotOperationsTest {


    @Test
    public void remindShouldSendMessageForEachChatDB() throws MalformedURLException {

        TelegramBotImpl bot = mock(TelegramBotImpl.class);
        BotOperations botOperations = new BotOperations(bot);

        URL url = new URL("https://example.com");
        List<ChatDB> chatDBList = Arrays.asList(new ChatDB(1l), new ChatDB(2l));


        botOperations.remind(url, chatDBList);

        verify(bot, times(2)).execute(any(SendMessage.class));
    }


}



