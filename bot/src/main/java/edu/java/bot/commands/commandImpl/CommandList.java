package edu.java.bot.commands.commandImpl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.command.Command;
import edu.java.bot.printer.Printer;
import edu.java.bot.reminder.Reminder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@edu.java.bot.annotation.Command
public class CommandList implements Command {
    private final Reminder reminder;

    @Autowired
    public CommandList(Reminder reminder) {
        this.reminder = reminder;
    }

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
        java.util.List<java.net.URL> trackingUrls = reminder.getUrls(update.message().chat().id());
        if (trackingUrls.isEmpty()) {
            return printer.getMessage(update.message()
                                            .chat()
                                            .id(), "There are no tracking urls");
        }
        StringBuilder answer = new StringBuilder("Tracking urls:\n");
        trackingUrls.forEach(url -> answer.append(printer.makeURL(url.toString()))
                                      .append("\n"));
        return printer.getMessage(update.message()
                                        .chat()
                                        .id(), answer.toString());
    }
}
