package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Util;
import edu.java.bot.print.MarkDownPrinter;
import edu.java.bot.print.Printer;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

public class CommandHelpTest {
    long ID = 1l;

    @Test
    @DisplayName("help command if no available command")
    public void handle_shouldWorkCorrectlyIfNoAvailableCommands() {
        List list = List.of();
        CommandHelp commandHelp = new CommandHelp(list);
        Printer printer = new MarkDownPrinter();
        Update update = Util.mockUpdate(ID, "");

        Map<String, Object> expectedAnswer = printer.getMessage(ID, "Available commands:\n").getParameters();

        Map<String, Object> givenAnswer = commandHelp.handle(update, printer).getParameters();

        Assertions.assertEquals(
            expectedAnswer,
            givenAnswer
        );
    }

    @Test
    @DisplayName("help command with available commands")
    public void handle_shouldWorkCorrectlyWithAvailableCommands() {
        List<Command> list = List.of(
            createMockCommand("c1", "d1"),
            createMockCommand("c2", "d2")
        );
        CommandHelp commandHelp = new CommandHelp(list);
        Printer printer = new MarkDownPrinter();
        Update update = Util.mockUpdate(ID, "");

        Map<String, Object> expectedAnswer =
            printer.getMessage(ID, "Available commands:\n*c1* : d1\n*c2* : d2\n").getParameters();
        Map<String, Object> givenAnswer = commandHelp.handle(update, printer).getParameters();

        Assertions.assertEquals(
            expectedAnswer,
            givenAnswer
        );
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
