package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.commandImpl.CommandStart;
import edu.java.bot.printer.Printer;
import edu.java.bot.reminder.Reminder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CommandStartTest {

    private static final long CHAT_ID = 111L;
    private Reminder reminder;
    private Printer printer;

    @BeforeEach
    public void setup() {
        reminder = Mockito.mock(Reminder.class);
        printer = new DefaultPrinter();
    }

    private Update createMockUpdate(String text) {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Mockito.when(update.message()).thenReturn(message);
        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(CHAT_ID);
        Mockito.when(message.text()).thenReturn(text);
        return update;
    }

    @Test
    @DisplayName("Handle should register user when executing start command")
    public void handle_shouldRegisterUser() {
        Update update = createMockUpdate("/start");

        CommandStart commandStart = new CommandStart(reminder);

        Assertions.assertEquals(
                printer.getMessage(CHAT_ID, "you successfully registered, to see available commands type /help")
                       .getParameters(),
                commandStart.handle(update, printer).getParameters()
        );
    }

    @Test
    @DisplayName("Handle should print message if user is already registered")
    public void handle_shouldPrintMessageIfUserAlreadyRegistered() {
        Update update = createMockUpdate("/start");

        Mockito.doThrow(new IllegalArgumentException("you are already registered")).when(reminder)
               .createUser(Mockito.any());

        CommandStart commandStart = new CommandStart(reminder);

        Assertions.assertEquals(
                printer.getMessage(CHAT_ID, "you are already registered").getParameters(),
                commandStart.handle(update, printer).getParameters()
        );
    }

    @Test
    @DisplayName("Handle should call reminder createUser when executing start command with URL")
    public void handle_shouldCallReminderCommand() {
        Update update = createMockUpdate("https://stackoverflow.com/");

        CommandStart commandStart = new CommandStart(reminder);

        commandStart.handle(update, printer);

        Mockito.verify(reminder, Mockito.times(1)).createUser(Mockito.any());
    }
}
