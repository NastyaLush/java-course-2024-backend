package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.LinksClient;
import edu.java.bot.exceptions.WebClientException;
import edu.java.bot.print.Printer;
import edu.java.bot.url.UrlService;
import edu.java.model.LinkResponse;
import edu.java.model.RemoveLinkRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandUntrack implements Command {
    public static final String FAILED_TO_UNTRACK_MESSAGE = "Failed to untrack ";
    public static final String URL_MESSAGE = "url";
    public static final int HTTP_OK_STATUS = 200;
    private final UrlService urlService;
    private final LinksClient linksClient;

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "untrack url";
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
            return printer.getMessage(chatId, "Please enter url to untrack");
        }

        if (urlService.isUrl(message)) {
            return deleteUrl(printer, chatId, message);
        }
        return printer.getMessage(
                chatId,
                "It's not an url"
        );
    }

    private SendMessage deleteUrl(Printer printer, Long chatId, String url) {
        ResponseEntity<LinkResponse> linkResponseResponseEntity;
        try {
            linkResponseResponseEntity = linksClient.linksDelete(chatId, new RemoveLinkRequest().link(URI.create(url)));
        } catch (WebClientException e) {
            return printer.getMessage(
                    chatId,
                    FAILED_TO_UNTRACK_MESSAGE
                            + printer.makeURL(URL_MESSAGE, url)
                            + "\nThere is an error occurred\n"
                            + printer.makeBold(e.getMessage())
            );
        }
        if (linkResponseResponseEntity.getStatusCode() == HttpStatusCode.valueOf(HTTP_OK_STATUS)) {
            return printer.getMessage(
                    chatId,
                    printer.makeURL(URL_MESSAGE, url) + " successfully removed"
            );
        } else {
            return printer.getMessage(
                    chatId,
                    FAILED_TO_UNTRACK_MESSAGE + printer.makeURL(URL_MESSAGE, url)
            );
        }
    }
}
