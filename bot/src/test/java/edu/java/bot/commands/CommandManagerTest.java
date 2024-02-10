package edu.java.bot.commands;

import static org.junit.jupiter.api.Assertions.*;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.annotation.CommandCallable;
import edu.java.bot.annotation.CurrentPrinter;
import edu.java.bot.commands.command.CallableCommand;
import edu.java.bot.printer.Printer;
import java.net.MalformedURLException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

class CommandManagerTest {


    @Test
    void constructor_shouldThrowExceptionIfNoPrinterBean() {
        // Mock ApplicationContext
        ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);

        // Mock Command beans
        CommandMock commandBean1 = new CommandMock();
        CommandCallableMock commandCallableBean1 = new CommandCallableMock();

        Mockito.when(applicationContext.getBean(CommandMock.class)).thenReturn(commandBean1);
        Mockito.when(applicationContext.getBean(CommandCallableMock.class)).thenReturn(commandCallableBean1);

        // Exception assertion
        assertThrows(IllegalArgumentException.class, () -> new CommandManager(applicationContext));
    }


    // Mock classes for testing
    @edu.java.bot.annotation.Command
    static class CommandMock implements edu.java.bot.commands.command.Command {

        @Override
        public String command() {
            return null;
        }

        @Override
        public String description() {
            return null;
        }

        @Override
        public SendMessage handle(Update update, Printer printer) {
            return null;
        }
    }

    @CommandCallable
    static class CommandCallableMock implements CallableCommand {

        @Override
        public SendMessage callableHandle(Update update, Printer printer) throws MalformedURLException {
            return null;
        }

        @Override
        public String command() {
            return null;
        }

        @Override
        public String description() {
            return null;
        }

        @Override
        public SendMessage handle(Update update, Printer printer) {
            return null;
        }
    }

    @CurrentPrinter
    static class PrinterMock implements Printer {

        @Override
        public SendMessage getMessage(Long chatId, String message) {
            return null;
        }

        @Override
        public String makeBold(String message) {
            return null;
        }

        @Override
        public String makeItalic(String message) {
            return null;
        }

        @Override
        public String makeURL(String message) {
            return null;
        }

        @Override
        public String makeURL(String text, String message) {
            return null;
        }

        @Override
        public String makeInlineCode(String message) {
            return null;
        }

        @Override
        public String makeFormatedInlineCode(String message) {
            return null;
        }
    }
}

