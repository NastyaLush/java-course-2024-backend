package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.LinksClient;
import edu.java.bot.print.Printer;
import edu.java.bot.url.UrlService;
import edu.java.model.AddLinkRequest;
import edu.java.model.LinkResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandTrack implements Command {
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
            ResponseEntity<LinkResponse> linkResponseResponseEntity = linksClient.linksPost(chatId, new AddLinkRequest().link(URI.create(message)));
            if (linkResponseResponseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                return printer.getMessage(
                        chatId,
                        "Url " + message + " is tracking"
                );
            } else {
                return printer.getMessage(
                        chatId,
                        "Failed to track " + message
                );
            }
        }
        return printer.getMessage(
                chatId,
                "It's not an url"
        );
    }
}
