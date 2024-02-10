package edu.java.bot.commands.commandImpl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.CommandManager;
import edu.java.bot.commands.command.Command;
import edu.java.bot.printer.Printer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@edu.java.bot.annotation.Command
public class CommandHelp implements Command {
    private final CommandManager commandManager;

    @Autowired
    @Lazy
    public CommandHelp(CommandManager commandManager) {
        this.commandManager = commandManager;
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
        StringBuilder answer = new StringBuilder("Available commands\n");
        commandManager.getCommandList()
                      .forEach(command -> answer
                              .append(printer.makeBold(command.command()))
                              .append(" : ")
                              .append(command.description())
                              .append("\n"));
        return printer.getMessage(update.message()
                                        .chat()
                                        .id(), answer.toString());
    }
}
