package edu.java.bot.manager;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.print.Printer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component public class UserManagerProcessorImpl implements UserMessageProcessor {
    private final Map<String, Command> commandsMap = new HashMap<>();
    private final Printer printer;
    private final Map<Long, String> chatCondition = new HashMap<>();

    @Autowired public UserManagerProcessorImpl(List<Command> commands, Printer printer) {
        this.printer = printer;
        initCommandsMap(commands);
    }

    private void initCommandsMap(List<Command> commands) {
        commands.forEach((command -> commandsMap.put(command.command(), command)));
    }

    @Override public SendMessage process(Update update) {
        Long chatId = update.message().chat().id();
        String message = update.message().text();
        if (commandsMap.containsKey(message)) {
            chatCondition.put(chatId, message);
            return commandsMap.get(message).handle(update, printer);
        }
        Command prevCommand = commandsMap.get(chatCondition.get(chatId));
        if (prevCommand != null && prevCommand.isCallabackable()) {
            return prevCommand.handle(update, printer);
        }
        return new SendMessage(chatId, "there is no this command((");
    }

}
