package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.commandImpl.CommandList;
import edu.java.bot.printer.Printer;
import edu.java.bot.reminder.Reminder;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommandListTest {

    private static final long CHAT_ID = 111L;

    private Reminder reminder;
    private Update update;
    private Printer printer;

    @BeforeEach
    public void setUp() {
        reminder = Mockito.mock(Reminder.class);
        update = Mockito.mock(Update.class);
        printer = new DefaultPrinter();
    }

    @Test
    @DisplayName("list command if empty urls list")
    public void handle_shouldReturnSpecialMessageIfNoTrackingUrls() {
        Mockito.when(reminder.getUrls(Mockito.anyLong())).thenReturn(List.of());

        Message message = Mockito.mock(Message.class);
        Mockito.when(update.message()).thenReturn(message);

        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(CHAT_ID);

        CommandList commandList = new CommandList(reminder);

        SendMessage expectedMessage = printer.getMessage(CHAT_ID, "There are no tracking urls");
        SendMessage actualMessage = commandList.handle(update, printer);

        Assertions.assertEquals(expectedMessage.getParameters(), actualMessage.getParameters(),
                "The message parameters should match.");
    }

    @Test
    @DisplayName("list command if not empty list")
    public void handle_shouldReturnTrackingUrls() throws MalformedURLException {
        Mockito.when(reminder.getUrls(Mockito.anyLong()))
               .thenReturn(List.of(new URL("https://chat.openai.com/"), new URL("https://www.baeldung.com")));

        Message message = Mockito.mock(Message.class);
        Mockito.when(update.message()).thenReturn(message);

        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(CHAT_ID);

        CommandList commandList = new CommandList(reminder);

        String expectedContent = "Tracking urls:\n" +
                                 printer.makeURL("https://chat.openai.com/") + "\n" +
                                 printer.makeURL("https://www.baeldung.com") + "\n";

        SendMessage expectedMessage = printer.getMessage(CHAT_ID, expectedContent);
        SendMessage actualMessage = commandList.handle(update, printer);

        Assertions.assertEquals(expectedMessage.getParameters(), actualMessage.getParameters(),
                "The message parameters should match.");
    }

    @Test
    @DisplayName("list command should call reminder command")
    public void handle_shouldCallReminderCommand() {
        CommandList commandList = new CommandList(reminder);
        Message message = Mockito.mock(Message.class);
        Mockito.when(update.message()).thenReturn(message);

        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(CHAT_ID);
        commandList.handle(update, printer).getParameters();

        Mockito.verify(reminder, Mockito.times(1)).getUrls(Mockito.anyLong());
    }
}
