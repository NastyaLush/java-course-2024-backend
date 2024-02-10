package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.command.Command;
import edu.java.bot.commands.commandImpl.CommandHelp;
import edu.java.bot.printer.Printer;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CommandHelpTest {

    private static final long CHAT_ID = 111L;

    private CommandManager commandManager;
    private Update update;
    private Printer printer;
    private CommandHelp commandHelp;

    @BeforeEach
    void setUp() {
        commandManager = Mockito.mock(CommandManager.class);
        update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(CHAT_ID);

        printer = new DefaultPrinter();
        commandHelp = new CommandHelp(commandManager);
    }

    @Test
    @DisplayName("help command if no available command")
    public void handle_shouldWorkCorrectlyIfNoAvailableCommands() {
        Mockito.when(commandManager.getCommandList()).thenReturn(List.of());

        Assertions.assertEquals(
                printer.getMessage(CHAT_ID, "Available commands\n").getParameters(),
                commandHelp.handle(update, printer).getParameters()
        );
    }

    @Test
    @DisplayName("help command with available commands")
    public void handle_shouldWorkCorrectlyWithAvailableCommands() {
        List<Command> commandList = List.of(
                createMockCommand("c1", "d1"),
                createMockCommand("c2", "d2")
        );

        Mockito.when(commandManager.getCommandList()).thenReturn(commandList);

        Assertions.assertEquals(
                printer.getMessage(CHAT_ID, "Available commands\nc1 : d1\nc2 : d2\n").getParameters(),
                commandHelp.handle(update, printer).getParameters()
        );
    }

    @Test
    @DisplayName("help command should call command manager command")
    public void handle_shouldCallCommandManager() {
        Mockito.when(commandManager.getCommandList()).thenReturn(List.of());

        commandHelp.handle(update, printer).getParameters();

        Mockito.verify(commandManager, Mockito.times(1)).getCommandList();
    }

    private Command createMockCommand(String command, String description) {
        return new Command() {
            @Override
            public String command() {
                return command;
            }

            @Override
            public String description() {
                return description;
            }

            @Override
            public SendMessage handle(Update update, Printer printer) {
                return null;
            }
        };
    }
}
