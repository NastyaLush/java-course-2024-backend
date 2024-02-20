package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.Util;
import edu.java.bot.print.MarkDownPrinter;
import edu.java.bot.print.Printer;
import edu.java.bot.url.UrlService;
import java.util.Map;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

public class CommandUntrackTest {
    long ID= 1L;

    @Test
    @DisplayName("handle should allow enter url if untrack command called")
    public void handle_shouldAllowEnterUrlIfCommandWasCalled(){
        UrlService urlService = new UrlService();
        Printer printer = new MarkDownPrinter();
        Update update = Util.mockUpdate(ID, "/untrack");
        CommandUntrack commandUntrack = new CommandUntrack(urlService);

        Map<String, Object> expectedAnswer = printer.getMessage(ID, "Please enter url to untrack").getParameters();
        Map<String, Object> givenAnswer = commandUntrack.handle(update, printer).getParameters();

        Assertions.assertEquals(
            expectedAnswer,
            givenAnswer
        );
    }
    @Test
    @DisplayName("handle should receive url after untrack command called")
    public void handle_shouldReceiveUrl(){
        UrlService urlService = new UrlService();
        Printer printer = new MarkDownPrinter();
        Update update = Util.mockUpdate(ID, "https://chat.openai.com/");
        CommandUntrack commandUntrack = new CommandUntrack(urlService);

        Map<String, Object> expectedAnswer =
            printer.getMessage(ID, "Url https://chat.openai.com/ removed from tracking").getParameters();
        Map<String, Object> givenAnswer = commandUntrack.handle(update, printer).getParameters();

        Assertions.assertEquals(
            expectedAnswer,
            givenAnswer
        );
    }
    @Test
    @DisplayName("handle should print error if url incorrect")
    public void handle_shouldPrintErrorIfUrlIncorrect(){
        UrlService urlService = new UrlService();
        Printer printer = new MarkDownPrinter();
        Update update = Util.mockUpdate(ID, "j");
        CommandUntrack commandUntrack = new CommandUntrack(urlService);

        Map<String, Object> expectedAnswer = printer.getMessage(ID, "It's not an url").getParameters();
        Map<String, Object> givenAnswer = commandUntrack.handle(update, printer).getParameters();

        Assertions.assertEquals(
            expectedAnswer,
            givenAnswer
        );
    }
}

