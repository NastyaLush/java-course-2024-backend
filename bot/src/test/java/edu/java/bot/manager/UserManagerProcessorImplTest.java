package edu.java.bot.manager;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Util;
import edu.java.bot.command.Command;
import edu.java.bot.print.Printer;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UserManagerProcessorImplTest {

    private static final long CHAT_ID = 1L;

    @Test
    public void process_shouldExecuteCommandIfExist() {
        Printer printer = Mockito.mock(Printer.class);

        String commandName = "command";
        SendMessage expectedSendMessage = Mockito.mock(SendMessage.class);
        Command command = createCommand(commandName, expectedSendMessage);
        List<Command> commands = List.of(command);
        Update update = Util.mockUpdate(CHAT_ID, commandName);

        UserManagerProcessorImpl userManagerProcessor = new UserManagerProcessorImpl(printer);
        commands.forEach(command1 -> command1.registerInUserMessageProcessor(userManagerProcessor));
        SendMessage actualSendMessage = userManagerProcessor.process(update);

        Assertions.assertEquals(expectedSendMessage, actualSendMessage);
    }

    @Test
    public void process_shouldCallPrevCommandIfCurrentNotSpecifiedAndPrevIsCallable() {
        Printer printer = Mockito.mock(Printer.class);

        String prevCommandName = "prevCommand";
        SendMessage expectedSendMessage = Mockito.mock(SendMessage.class);
        Command prevCommand = createCommand(prevCommandName, expectedSendMessage, true);
        List<Command> commands = List.of(prevCommand);
        Update update = Util.mockUpdate(CHAT_ID, "hh");
        Update updatePrev = Util.mockUpdate(CHAT_ID, prevCommandName);

        UserManagerProcessorImpl userManagerProcessor = new UserManagerProcessorImpl(printer);
        commands.forEach(command -> command.registerInUserMessageProcessor(userManagerProcessor));
        userManagerProcessor.process(updatePrev);
        SendMessage actualSendMessage = userManagerProcessor.process(update);

        Assertions.assertEquals(expectedSendMessage, actualSendMessage);
    }

    @Test
    public void process_shouldPrintErrorMessageIfNoThisCommand() {
        Printer printer = Mockito.mock(Printer.class);

        String commandName = "command";
        SendMessage expectedSendMessage = new SendMessage(CHAT_ID, "there is no this command((");
        List<Command> commands = List.of(Mockito.mock(Command.class), Mockito.mock(Command.class));
        Update update = Util.mockUpdate(CHAT_ID, commandName);

        UserManagerProcessorImpl userManagerProcessor = new UserManagerProcessorImpl(printer);
        commands.forEach(command -> command.registerInUserMessageProcessor(userManagerProcessor));
        SendMessage actualSendMessage = userManagerProcessor.process(update);

        Assertions.assertEquals(expectedSendMessage.getParameters(), actualSendMessage.getParameters());
    }

    private Command createCommand(String commandName, SendMessage expectedSendMessage) {
        return createCommand(commandName, expectedSendMessage, false);
    }

    private Command createCommand(String commandName, SendMessage expectedSendMessage, boolean isCallable) {
        return new Command() {
            @Override
            public String command() {
                return commandName;
            }

            @Override
            public String description() {
                return null;
            }

            @Override
            public SendMessage handle(Update update, Printer printer) {
                return expectedSendMessage;
            }

            @Override
            public boolean isCallabackable() {
                return isCallable;
            }
        };
    }
}

