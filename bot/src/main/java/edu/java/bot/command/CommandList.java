package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.LinksClient;
import edu.java.bot.print.Printer;
import edu.java.model.ListLinksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandList implements Command {
    private final LinksClient linksClient;


    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "get all tracked urls";
    }

    @Override
    public SendMessage handle(Update update, Printer printer) {
        StringBuilder trackingUrls = new StringBuilder();
        Long id = update.message()
                        .chat()
                        .id();
        ResponseEntity<ListLinksResponse> listLinksResponseResponseEntity = linksClient.linksGet(id);
        if (listLinksResponseResponseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            listLinksResponseResponseEntity.getBody()
                                           .getLinks()
                                           .forEach(
                                                   linkResponse -> trackingUrls.append(linkResponse.getUrl())
                                                                               .append("\n")
                                           );
            return printer.getMessage(
                    id,
                    "Tracking urls:\n" + trackingUrls
            );
        } else {
            return printer.getMessage(
                    id,
                    "There is an error occurred " + listLinksResponseResponseEntity.getStatusCode()
            );
        }
    }
}
