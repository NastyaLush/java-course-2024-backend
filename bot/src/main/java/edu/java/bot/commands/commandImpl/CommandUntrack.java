package edu.java.bot.commands.commandImpl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.annotation.Command;
import edu.java.bot.commands.command.CallableCommand;
import edu.java.bot.printer.Printer;
import edu.java.bot.reminder.Reminder;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Command
@edu.java.bot.annotation.CommandCallable
public class CommandUntrack implements CallableCommand {
    private final Reminder reminder;

    @Autowired
    public CommandUntrack(Reminder reminder) {
        this.reminder = reminder;
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
    public SendMessage handle(Update update, Printer printer) {
        return printer.getMessage(update.message()
                                        .chat()
                                        .id(), "please enter url");
    }

    @Override
    public SendMessage callableHandle(Update update, Printer printer) throws MalformedURLException {
        try {
            reminder.deleteUrl(update.message().chat().id(), new URL(update.message().text()));
        } catch (IllegalArgumentException illegalArgumentException) {
            return printer.getMessage(update.message()
                                            .chat()
                                            .id(), illegalArgumentException.getMessage());
        }
        return printer.getMessage(update.message()
                                        .chat()
                                        .id(), "url is successfully untracked");
    }
}
