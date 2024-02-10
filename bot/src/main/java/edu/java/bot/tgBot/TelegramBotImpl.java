package edu.java.bot.tgBot;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.configuration.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotImpl extends TelegramBot {
    @Autowired
    public TelegramBotImpl(ApplicationConfig applicationConfig) {
        super(applicationConfig.telegramToken());
    }
}
