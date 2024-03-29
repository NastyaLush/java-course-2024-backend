package edu.java.bot.manager;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;

public interface UserMessageProcessor {

    void register(Command command);

    SendMessage process(Update update);

}
