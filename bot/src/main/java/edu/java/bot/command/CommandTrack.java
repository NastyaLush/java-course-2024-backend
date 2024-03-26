package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.LinksClient;
import edu.java.bot.exceptions.CustomWebClientException;
import edu.java.bot.print.Printer;
import edu.java.bot.url.UrlService;
import edu.java.model.AddLinkRequest;
import edu.java.model.LinkResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class CommandTrack implements Command {
    public static final int HTTP_OK_STATUS = 200;
    public static final String FAILED_TO_TRACK_MESSAGE = "Failed to track ";
    public static final String URL_MESSAGE = "url";
    private final UrlService urlService;
    private final LinksClient linksClient;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "start track url";
    }

    @Override
    public boolean isCallabackable() {
        return true;
    }

    @Override
    public SendMessage handle(Update update, Printer printer) {
        Long chatId = update.message()
                            .chat()
                            .id();
        String message = update.message()
                               .text();
        if (message.equals(command())) {
            return printer.getMessage(chatId, "Please enter url to track");
        }
        if (urlService.isUrl(message)) {
            return createUrl(printer, chatId, message);
        }
        return printer.getMessage(
                chatId,
                "It's not an url"
        );
    }

    private SendMessage createUrl(Printer printer, Long chatId, String url) {
        ResponseEntity<LinkResponse> linkResponseResponseEntity;
        try {
            linkResponseResponseEntity = linksClient.linksPost(chatId, new AddLinkRequest().link(URI.create(url)));
        } catch (CustomWebClientException e) {
            return printer.getMessage(
                    chatId,
                    FAILED_TO_TRACK_MESSAGE
                            + printer.makeURL(URL_MESSAGE, url)
                            + "\nThere is an error occurred\n"
                            + printer.makeBold(e.getMessage())
            );
        }
        if (linkResponseResponseEntity.getStatusCode() == HttpStatusCode.valueOf(HTTP_OK_STATUS)) {
            return printer.getMessage(
                    chatId,
                    printer.makeURL("Url", url) + " is tracking"
            );
        } else {
            return printer.getMessage(
                    chatId,
                    FAILED_TO_TRACK_MESSAGE + printer.makeURL(URL_MESSAGE, url)
            );
        }
    }
}
