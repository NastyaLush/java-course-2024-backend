package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.print.Printer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandHelp implements Command {
    private final List<Command> availableCommands;

    @Autowired
    public CommandHelp(List<Command> availableCommands) {
        this.availableCommands = availableCommands;
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "show available commands";
    }

    @Override
    public SendMessage handle(Update update, Printer printer) {
        StringBuilder answer = new StringBuilder("Available commands:\n");
        availableCommands.forEach(command -> answer.append(printer.makeBold(command.command()))
                                                   .append(" : ")
                                                   .append(command.description())
                                                   .append("\n"));
        return printer.getMessage(
                update.message()
                      .chat()
                      .id(), answer.toString()
        );
    }
}
