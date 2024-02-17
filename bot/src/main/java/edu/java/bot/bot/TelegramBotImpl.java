package edu.java.bot.bot;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.command.Command;
import edu.java.bot.configuration.ApplicationConfig;
import java.io.IOException;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class TelegramBotImpl extends TelegramBot {
    @Autowired
    public TelegramBotImpl(ApplicationConfig applicationConfig, BotListener botListener, List<Command> commands) {
        super(applicationConfig.telegramToken());
        this.setUpdatesListener(botListener);
        addCommandsMenu(commands);
    }

    private void addCommandsMenu(List<Command> commands) {

        SetMyCommands setMyCommands = new SetMyCommands(
            commands.stream().map(Command::toApiCommand)
                .toArray(BotCommand[]::new)).languageCode("en");

        this.execute(setMyCommands, new Callback<SetMyCommands, BaseResponse>() {
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
