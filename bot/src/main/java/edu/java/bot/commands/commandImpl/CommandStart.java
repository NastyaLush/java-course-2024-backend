package edu.java.bot.commands.commandImpl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.command.Command;
import edu.java.bot.db.ChatDB;
import edu.java.bot.printer.Printer;
import edu.java.bot.reminder.Reminder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@edu.java.bot.annotation.Command
public class CommandStart implements Command {
    final Reminder reminder;

    @Autowired
    public CommandStart(Reminder reminder) {
        this.reminder = reminder;
    }

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "register";
    }

    @Override
    public SendMessage handle(Update update, Printer printer) {
        try {
            reminder.createUser(new ChatDB(update.message().chat().id()));
        } catch (IllegalArgumentException illegalArgumentException) {
            return new SendMessage(update.message().chat().id(), illegalArgumentException.getMessage());
        }
        return printer.getMessage(
                update.message()
                      .chat()
                      .id(),
                "you successfully registered, to see available commands type /help"
        );
    }
}
