package edu.java.bot.commands.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.printer.Printer;

public interface Command {
    String command();

    String description();

    SendMessage handle(Update update, Printer printer);

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
