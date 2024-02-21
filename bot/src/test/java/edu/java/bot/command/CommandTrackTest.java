package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.Util;
import edu.java.bot.print.MarkDownPrinter;
import edu.java.bot.print.Printer;
import edu.java.bot.url.UrlService;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

public class CommandTrackTest {
    long ID= 1L;

    @Test
    @DisplayName("handle should allow enter url if track command called")
    public void handle_shouldAllowEnterUrlIfCommandWasCalled(){
        UrlService urlService = Mockito.mock(UrlService.class);
        Printer printer = new MarkDownPrinter();
        Update update = Util.mockUpdate(ID, "/track");
        CommandTrack commandTrack = new CommandTrack(urlService);

        Map<String, Object> expectedAnswer = printer.getMessage(ID, "Please enter url to track").getParameters();
        Map<String, Object> givenAnswer = commandTrack.handle(update, printer).getParameters();

        Assertions.assertEquals(
            expectedAnswer,
            givenAnswer
        );
    }
    @Test
    @DisplayName("handle should receive url after track command called")
    public void handle_shouldReceiveUrl(){
        UrlService urlService = new UrlService();
        Printer printer = new MarkDownPrinter();
        Update update = Util.mockUpdate(ID, "https://chat.openai.com/");
        CommandTrack commandTrack = new CommandTrack(urlService);

        Map<String, Object> expectedAnswer =
            printer.getMessage(ID, "Url https://chat.openai.com/ is tracking").getParameters();
        Map<String, Object> givenAnswer = commandTrack.handle(update, printer).getParameters();

        Assertions.assertEquals(
            expectedAnswer,
            givenAnswer
        );
    }
    @Test
    @DisplayName("handle should print error if url incorrect")
    public void handle_shouldPrintErrorIfUrlIncorrect(){
        UrlService urlService = Mockito.mock(UrlService.class);
        Printer printer = new MarkDownPrinter();
        Update update = Util.mockUpdate(ID, "j");
        CommandTrack commandTrack = new CommandTrack(urlService);

        Map<String, Object> expectedAmswer = printer.getMessage(ID, "It's not an url").getParameters();
        Map<String, Object> givenAnswer = commandTrack.handle(update, printer).getParameters();
        Assertions.assertEquals(
            expectedAmswer,
            givenAnswer
        );
    }
}
