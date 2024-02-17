package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.print.Printer;
import org.springframework.stereotype.Component;

@Component public class CommandList implements Command {

    @Override public String command() {
        return "/list";
    }

    @Override public String description() {
        return "get all tracked urls";
    }

    @Override public SendMessage handle(Update update, Printer printer) {
        return printer.getMessage(
            update.message().chat().id(),
            "list: i'm just being developed, this command will be added in the next versions"
        );
    }
}
