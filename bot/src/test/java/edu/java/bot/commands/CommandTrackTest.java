package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.commandImpl.CommandTrack;
import edu.java.bot.printer.Printer;
import edu.java.bot.reminder.Reminder;
import java.net.MalformedURLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CommandTrackTest {

    private static final long CHAT_ID = 111L;

    private Reminder createMockReminder() {
        return Mockito.mock(Reminder.class);
    }

    private Update createMockUpdate(String text) {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(text);
        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(CHAT_ID);
        return update;
    }

    @Test
    @DisplayName("track command should print special message if url wrong")
    public void callableHandle_shouldTrackUrls() throws MalformedURLException {
        Reminder reminder = createMockReminder();
        Printer printer = new DefaultPrinter();
        Update update = createMockUpdate("https://stackoverflow.com/");

        Mockito.doThrow(new IllegalArgumentException("something went wrong, please check url and network connection")).when(reminder)
               .addUrl(Mockito.anyLong(), Mockito.any());
        CommandTrack commandTrack = new CommandTrack(reminder);

        Assertions.assertEquals(
                printer.getMessage(CHAT_ID, "something went wrong, please check url and network connection")
                       .getParameters(),
                commandTrack.callableHandle(update, printer).getParameters()
        );
    }

    @Test
    @DisplayName("track command should track user url")
    public void callableHandle_shouldRegisterUser() throws MalformedURLException {
        Reminder reminder = createMockReminder();
        Printer printer = new DefaultPrinter();
        Update update = createMockUpdate("https://stackoverflow.com/");

        CommandTrack commandTrack = new CommandTrack(reminder);

        Assertions.assertEquals(
                printer.getMessage(CHAT_ID, "url is successfully tracked").getParameters(),
                commandTrack.callableHandle(update, printer).getParameters()
        );
    }

    @Test
    @DisplayName("track command should allow typing url")
    public void handle_shouldAllowTypeUrl() {
        Reminder reminder = createMockReminder();
        Printer printer = new DefaultPrinter();
        Update update = createMockUpdate("https://stackoverflow.com/");

        CommandTrack commandTrack = new CommandTrack(reminder);

        Assertions.assertEquals(
                printer.getMessage(CHAT_ID, "please enter url").getParameters(),
                commandTrack.handle(update, printer).getParameters()
        );
    }

    @Test
    @DisplayName("track command should call reminder command")
    public void handle_shouldCallReminderCommand() throws MalformedURLException {
        Reminder reminder = createMockReminder();
        Printer printer = new DefaultPrinter();
        Update update = createMockUpdate("https://stackoverflow.com/");

        CommandTrack commandTrack = new CommandTrack(reminder);

        commandTrack.callableHandle(update, printer).getParameters();
        Mockito.verify(reminder, Mockito.times(1)).addUrl(Mockito.anyLong(), Mockito.any());
    }


}
