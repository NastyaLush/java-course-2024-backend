package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.manager.UserMessageProcessor;
import edu.java.bot.print.Printer;
import org.springframework.beans.factory.annotation.Autowired;

public interface Command {
    String command();

    String description();

    SendMessage handle(Update update, Printer printer);

    default boolean isCallabackable() {
        return false;
    }
    @Autowired
    default void registerInUserMessageProcessor(UserMessageProcessor userMessageProcessor){
        userMessageProcessor.register(this);
    }

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
