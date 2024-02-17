package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.manager.UserManagerProcessorImpl;
import edu.java.bot.manager.UserManagerProcessorImplTest;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;
import static edu.java.bot.Util.mockUpdate;

public class BotListenerImplTest {

    @Test
    @DisplayName("process should call userManagerProcessor update count times")
    public void process_shouldCallUserManagerProcessorProcessMethod(){
        TelegramBot telegramBot = Mockito.mock(TelegramBot.class);
        UserManagerProcessorImpl userManagerProcessor = Mockito.mock(UserManagerProcessorImpl.class);
        Mockito.when(userManagerProcessor.process(Mockito.any(Update.class)))
            .thenReturn(null);

        BotListenerImpl botListener = new BotListenerImpl(userManagerProcessor, telegramBot);
        Update update = mockUpdate(1L,"");
        List<Update> updates = List.of(update);


        botListener.process(updates);


        Mockito.verify(userManagerProcessor, Mockito.times(updates.size())).process(Mockito.any());

    }

}
