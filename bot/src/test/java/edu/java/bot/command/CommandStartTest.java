package edu.java.bot.command;

import com.example.exceptions.CustomWebClientException;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.Util;
import edu.java.bot.client.TgChatClient;
import edu.java.bot.print.MarkDownPrinter;
import edu.java.bot.print.Printer;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class CommandStartTest {
    private static final String EXAMPLE_URL = "";
    long ID = 1L;


    @Test
    @DisplayName("handle should register user after start command called")
    public void handle_shouldRegisterUser() {
        TgChatClient tgChatClient = Mockito.mock(TgChatClient.class);
        ResponseEntity<Void> voidResponseEntity = Mockito.mock(ResponseEntity.class);
        Mockito.when(tgChatClient.tgChatIdPost(Mockito.any()))
               .thenReturn(voidResponseEntity);
        Mockito.when(voidResponseEntity.getStatusCode())
               .thenReturn(HttpStatusCode.valueOf(200));
        Printer printer = new MarkDownPrinter();
        CommandStart commandStart = new CommandStart(tgChatClient);
        Update update = Util.mockUpdate(ID, EXAMPLE_URL);

        Map<String, Object> expectedAnswer =
                printer.getMessage(ID, "Welcome to our bot and congratulations, you were successfully registered")
                       .getParameters();
        Map<String, Object> givenAnswer = commandStart.handle(update, printer)
                                                      .getParameters();

        Assertions.assertEquals(
                expectedAnswer,
                givenAnswer
        );
    }

    @Test
    @DisplayName("handle should print error if response status not 200")
    public void handle_shouldPrintErrorMessageIfResponseStatusNot200() {
        TgChatClient tgChatClient = Mockito.mock(TgChatClient.class);
        ResponseEntity<Void> voidResponseEntity = Mockito.mock(ResponseEntity.class);
        Mockito.when(tgChatClient.tgChatIdPost(Mockito.any()))
               .thenReturn(voidResponseEntity);
        Mockito.when(voidResponseEntity.getStatusCode())
               .thenReturn(HttpStatusCode.valueOf(400));
        Printer printer = new MarkDownPrinter();
        CommandStart commandStart = new CommandStart(tgChatClient);
        Update update = Util.mockUpdate(ID, EXAMPLE_URL);

        Map<String, Object> expectedAnswer =
                printer.getMessage(ID, "There is an error occurred\n" + printer.makeBold(voidResponseEntity.getStatusCode()
                                                                                                           .toString()))
                       .getParameters();
        Map<String, Object> givenAnswer = commandStart.handle(update, printer)
                                                      .getParameters();

        Assertions.assertEquals(
                expectedAnswer,
                givenAnswer
        );
    }

    @Test
    @DisplayName("handle should print error if request or response failed")
    public void handle_shouldPrintErrorMessageIfRequestOrResponseFailed() {
        TgChatClient tgChatClient = Mockito.mock(TgChatClient.class);
        String errorMessage = "message";
        Mockito.when(tgChatClient.tgChatIdPost(Mockito.any()))
               .thenThrow(new CustomWebClientException(errorMessage));

        Printer printer = new MarkDownPrinter();
        CommandStart commandStart = new CommandStart(tgChatClient);
        Update update = Util.mockUpdate(ID, EXAMPLE_URL);

        Map<String, Object> expectedAnswer =
                printer.getMessage(ID, "There is an error occurred\n" + printer.makeBold(errorMessage))
                       .getParameters();
        Map<String, Object> givenAnswer = commandStart.handle(update, printer)
                                                      .getParameters();

        Assertions.assertEquals(
                expectedAnswer,
                givenAnswer
        );
    }
}
