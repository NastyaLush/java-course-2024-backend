package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.Util;
import edu.java.bot.client.LinksClient;
import edu.java.bot.print.MarkDownPrinter;
import edu.java.bot.print.Printer;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class CommandListTest {
    private static final String EXAMPLE_URL = "";
    long ID = 1L;

    @Test
    @DisplayName("handle print url list user after list command called")
    public void handle_shouldRegisterUser() {
        LinksClient linksClient = Mockito.mock(LinksClient.class);
        ResponseEntity<ListLinksResponse> listLinksResponseResponseEntity = Mockito.mock(ResponseEntity.class);
        ListLinksResponse listLinksResponse = Mockito.mock(ListLinksResponse.class);
        Mockito.when(linksClient.linksGet(Mockito.any()))
               .thenReturn(listLinksResponseResponseEntity);
        Mockito.when(listLinksResponseResponseEntity.getStatusCode())
               .thenReturn(HttpStatusCode.valueOf(200));
        Mockito.when(listLinksResponseResponseEntity.getBody())
               .thenReturn(listLinksResponse);
        LinkResponse linkResponse = new LinkResponse();
        linkResponse.setUrl(URI.create(EXAMPLE_URL));
        Mockito.when(listLinksResponse.getLinks())
               .thenReturn(List.of(linkResponse));
        Printer printer = new MarkDownPrinter();
        CommandList commandList = new CommandList(linksClient);
        Update update = Util.mockUpdate(ID, EXAMPLE_URL);

        Map<String, Object> expectedAnswer =
                printer.getMessage(ID, "Tracking urls:\n" + EXAMPLE_URL + "\n")
                       .getParameters();
        Map<String, Object> givenAnswer = commandList.handle(update, printer)
                                                     .getParameters();

        Assertions.assertEquals(
                expectedAnswer,
                givenAnswer
        );
    }

    @Test
    @DisplayName("handle should print error if response status not 200")
    public void handle_shouldPrintErrorMessageIfResponseStatusNot200() {
        LinksClient linksClient = Mockito.mock(LinksClient.class);
        ResponseEntity<ListLinksResponse> listLinksResponseResponseEntity = Mockito.mock(ResponseEntity.class);
        Mockito.when(linksClient.linksGet(Mockito.any()))
               .thenReturn(listLinksResponseResponseEntity);
        Mockito.when(listLinksResponseResponseEntity.getStatusCode())
               .thenReturn(HttpStatusCode.valueOf(400));


        Printer printer = new MarkDownPrinter();
        CommandList commandList = new CommandList(linksClient);
        Update update = Util.mockUpdate(ID, EXAMPLE_URL);

        Map<String, Object> expectedAnswer =
                printer.getMessage(ID, "There is an error occurred\n" + printer.makeBold(listLinksResponseResponseEntity.getStatusCode()
                                                                                                                        .toString()))
                       .getParameters();
        Map<String, Object> givenAnswer = commandList.handle(update, printer)
                                                     .getParameters();

        Assertions.assertEquals(
                expectedAnswer,
                givenAnswer
        );
    }

    @Test
    @DisplayName("handle should print error if request or response failed")
    public void handle_shouldPrintErrorMessageIfRequestOrResponseFailed() {
        LinksClient linksClient = Mockito.mock(LinksClient.class);
        String errorMessage = "message";
        Mockito.when(linksClient.linksGet(Mockito.any()))
               .thenThrow(new CustomWebClientException(errorMessage));

        Printer printer = new MarkDownPrinter();
        CommandList commandList = new CommandList(linksClient);
        Update update = Util.mockUpdate(ID, EXAMPLE_URL);

        Map<String, Object> expectedAnswer =
                printer.getMessage(ID, "There is an error occurred\n" + printer.makeBold(errorMessage))
                       .getParameters();
        Map<String, Object> givenAnswer = commandList.handle(update, printer)
                                                     .getParameters();

        Assertions.assertEquals(
                expectedAnswer,
                givenAnswer
        );
    }
}
