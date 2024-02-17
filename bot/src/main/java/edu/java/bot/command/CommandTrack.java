package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.print.Printer;
import edu.java.bot.url.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandTrack implements Command {
    private final UrlService urlService;

    @Autowired
    public CommandTrack(UrlService urlService) {
        this.urlService = urlService;
    }

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
        Long chatId = update.message().chat().id();
        String message = update.message().text();
        if (message.equals(command())) {
            return printer.getMessage(chatId, "Please enter url to track");
        }
        if (urlService.isUrl(message)) {
            return printer.getMessage(
                chatId,
                "Url "+ message+ " is tracking"
            );
        }
        return printer.getMessage(
            chatId,
            "It's not an url"
        );
    }
}
