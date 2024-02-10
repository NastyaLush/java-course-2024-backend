package edu.java.bot.tgBot;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.commands.CommandManager;
import edu.java.bot.commands.command.CallableCommand;
import edu.java.bot.commands.command.Command;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class BotStarter {
    private final TelegramBotImpl bot;
    private final CommandManager commandManager;
    private final Map<Long, String> prevCommand = new HashMap<>();

    @Autowired
    public BotStarter(TelegramBotImpl bot, CommandManager commandManager) {
        this.bot = bot;
        this.commandManager = commandManager;
    }

    @PostConstruct
    public void start() {
        addCommandsMenu();
        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                long chatId = update.message().chat().id();
                String receivedText = update.message().text();

                Optional<Command> inputCommand =
                        findCommand(commandManager.getCommandList().stream(), receivedText::equals);
                if (inputCommand.isPresent()) {
                    prevCommand.put(chatId, receivedText);
                    bot.execute(inputCommand.get().handle(update, commandManager.getPrinter()));
                } else {
                    String unknownCommandMessage = "unknown command, type /help to get available commands";
                    if (prevCommand.get(chatId) != null) {
                        Optional<CallableCommand> optionalCallableCommand =
                                findCommand(commandManager.getCallableCommandList().stream(),
                                        prevCommand.get(chatId)::equals);
                        if (optionalCallableCommand.isPresent()) {
                            try {
                                bot.execute(optionalCallableCommand.get().callableHandle(update,
                                        commandManager.getPrinter()));
                                prevCommand.remove(chatId);
                            } catch (MalformedURLException e) {
                                bot.execute(new SendMessage(chatId, "impossible to parse url"));
                            }
                        } else {
                            bot.execute(new SendMessage(chatId, unknownCommandMessage));
                        }
                    } else {
                        bot.execute(new SendMessage(chatId, unknownCommandMessage));
                    }
                }

            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, e -> {
            if (e.response() != null) {
                e.response().errorCode();
                e.response().description();
            } else {
                log.error(e.getMessage());
            }
        });
    }


    private <T extends Command> Optional<T> findCommand(java.util.stream.Stream<T> stream,
                                                        java.util.function.Predicate<String> predicate) {
        return stream.filter((current) -> predicate.test(current.command())).findFirst();
    }

    private void addCommandsMenu() {

        SetMyCommands setMyCommands = new SetMyCommands(
                commandManager.getCommandList().stream().map(Command::toApiCommand)
                              .toArray(BotCommand[]::new)).languageCode("en");

        bot.execute(setMyCommands, new Callback<SetMyCommands, BaseResponse>() {
            @Override
            public void onResponse(SetMyCommands setMyCommands, BaseResponse baseResponse) {
                log.info("menu buttons added");
            }

            @Override
            public void onFailure(SetMyCommands setMyCommands, IOException e) {
                log.error("failed to add menu buttons");
            }
        });
    }


}
