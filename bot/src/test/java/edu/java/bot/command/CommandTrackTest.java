package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.Util;
import edu.java.bot.client.LinksClient;
import edu.java.bot.exceptions.CustomWebClientException;
import edu.java.bot.print.MarkDownPrinter;
import edu.java.bot.print.Printer;
import edu.java.bot.url.UrlService;
import edu.java.model.LinkResponse;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class CommandTrackTest {
    public static final String EXAMPLE_URL = "https://chat.openai.com/";
    long ID = 1L;

    @Test
    @DisplayName("handle should allow enter url if track command called")
    public void handle_shouldAllowEnterUrlIfCommandWasCalled() {
        UrlService urlService = Mockito.mock(UrlService.class);
        LinksClient linksClient = Mockito.mock(LinksClient.class);
        Printer printer = new MarkDownPrinter();
        Update update = Util.mockUpdate(ID, "/track");
        CommandTrack commandTrack = new CommandTrack(urlService, linksClient);

        Map<String, Object> expectedAnswer = printer.getMessage(ID, "Please enter url to track")
                                                    .getParameters();
        Map<String, Object> givenAnswer = commandTrack.handle(update, printer)
                                                      .getParameters();

        Assertions.assertEquals(
                expectedAnswer,
                givenAnswer
        );
    }

    @Test
    @DisplayName("handle should receive url after track command called")
    public void handle_shouldReceiveUrl() throws CustomWebClientException {
        UrlService urlService = new UrlService();
        LinksClient linksClient = Mockito.mock(LinksClient.class);
        ResponseEntity<LinkResponse> linkResponseResponseEntity = Mockito.mock(ResponseEntity.class);
        Mockito.when(linksClient.linksPost(Mockito.any(), Mockito.any()))
               .thenReturn(linkResponseResponseEntity);
        Mockito.when(linkResponseResponseEntity.getStatusCode())
               .thenReturn(HttpStatusCode.valueOf(200));
        Printer printer = new MarkDownPrinter();
        Update update = Util.mockUpdate(ID, EXAMPLE_URL);
        CommandTrack commandTrack = new CommandTrack(urlService, linksClient);

        Map<String, Object> expectedAnswer =
                printer.getMessage(ID, printer.makeURL("Url", EXAMPLE_URL)+" is tracking")
                       .getParameters();
        Map<String, Object> givenAnswer = commandTrack.handle(update, printer)
                                                      .getParameters();

        Assertions.assertEquals(
                expectedAnswer,
                givenAnswer
        );
    }

    @Test
    @DisplayName("handle should print error if response status not 200")
    public void handle_shouldPrintErrorMessageIfResponseStatusNot200() throws CustomWebClientException {
        UrlService urlService = new UrlService();
        LinksClient linksClient = Mockito.mock(LinksClient.class);
        ResponseEntity<LinkResponse> linkResponseResponseEntity = Mockito.mock(ResponseEntity.class);
        Mockito.when(linksClient.linksPost(Mockito.any(), Mockito.any()))
               .thenReturn(linkResponseResponseEntity);
        Mockito.when(linkResponseResponseEntity.getStatusCode())
               .thenReturn(HttpStatusCode.valueOf(400));
        Printer printer = new MarkDownPrinter();
        Update update = Util.mockUpdate(ID, EXAMPLE_URL);
        CommandTrack commandTrack = new CommandTrack(urlService, linksClient);

        Map<String, Object> expectedAnswer =
                printer.getMessage(ID, "Failed to track " + printer.makeURL("url", EXAMPLE_URL))
                       .getParameters();
        Map<String, Object> givenAnswer = commandTrack.handle(update, printer)
                                                      .getParameters();

        Assertions.assertEquals(
                expectedAnswer,
                givenAnswer
        );
    }

    @Test
    @DisplayName("handle should print error if request or response failed")
    public void handle_shouldPrintErrorMessageIfRequestOrResponseFailed() throws CustomWebClientException {
        UrlService urlService = new UrlService();
        LinksClient linksClient = Mockito.mock(LinksClient.class);
        String errorMessage = "message";
        Mockito.when(linksClient.linksPost(Mockito.any(), Mockito.any()))
               .thenThrow(new CustomWebClientException(errorMessage));
        Printer printer = new MarkDownPrinter();
        Update update = Util.mockUpdate(ID, EXAMPLE_URL);
        CommandTrack commandTrack = new CommandTrack(urlService, linksClient);

        Map<String, Object> expectedAnswer =
                printer.getMessage(ID, "Failed to track " + printer.makeURL("url", EXAMPLE_URL) + "\nThere is an error occurred\n" + printer.makeBold(errorMessage)
                       )
                       .getParameters();
        Map<String, Object> givenAnswer = commandTrack.handle(update, printer)
                                                      .getParameters();

        Assertions.assertEquals(
                expectedAnswer,
                givenAnswer
        );
    }

    @Test
    @DisplayName("handle should print error if url incorrect")
    public void handle_shouldPrintErrorIfUrlIncorrect() {
        UrlService urlService = Mockito.mock(UrlService.class);
        LinksClient linksClient = Mockito.mock(LinksClient.class);
        Printer printer = new MarkDownPrinter();
        Update update = Util.mockUpdate(ID, "j");
        CommandTrack commandTrack = new CommandTrack(urlService, linksClient);

        Map<String, Object> expectedAmswer = printer.getMessage(ID, "It's not an url")
                                                    .getParameters();
        Map<String, Object> givenAnswer = commandTrack.handle(update, printer)
                                                      .getParameters();
        Assertions.assertEquals(
                expectedAmswer,
                givenAnswer
        );
    }
}
