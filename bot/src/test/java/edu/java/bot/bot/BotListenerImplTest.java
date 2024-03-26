package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.manager.UserManagerProcessorImpl;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static edu.java.bot.Util.mockUpdate;

public class BotListenerImplTest {

    @Test
    @DisplayName("process should call userManagerProcessor update count times")
    public void process_shouldCallUserManagerProcessorProcessMethod() {
        TelegramBot telegramBot = Mockito.mock(TelegramBot.class);
        UserManagerProcessorImpl userManagerProcessor = Mockito.mock(UserManagerProcessorImpl.class);
        Mockito.when(userManagerProcessor.process(Mockito.any(Update.class)))
               .thenReturn(null);

        Update update = mockUpdate(1L, "");
        List<Update> updates = List.of(update);
        BotListenerImpl botListener = new BotListenerImpl(userManagerProcessor, telegramBot);
        botListener.process(updates);


        Mockito.verify(userManagerProcessor, Mockito.times(updates.size()))
               .process(Mockito.any());

    }

}
