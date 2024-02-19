package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.print.Printer;
import edu.java.bot.url.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandUntrack implements Command {
    private final UrlService urlService;

    @Autowired
    public CommandUntrack(UrlService urlService) {
        this.urlService = urlService;
    }

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
        Long chatId = update.message().chat().id();
        String message = update.message().text();
        if (message.equals(command())) {
            return printer.getMessage(chatId, "Please enter url to untrack");
        }

        if (urlService.isUrl(message)) {
            return printer.getMessage(
                    chatId,
                    "Url " + message + " removed from tracking"
            );
        }
        return printer.getMessage(
                chatId,
                "It's not an url"
        );
    }
}
