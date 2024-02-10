package edu.java.bot.tgBot;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.db.ChatDB;
import java.net.URL;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotOperations {
    private final TelegramBotImpl bot;

    @Autowired
    public BotOperations(TelegramBotImpl bot) {
        this.bot = bot;
    }

    public void remind(URL url, List<ChatDB> chatDBList) {
        chatDBList
                .forEach((chatDB) -> {
                    bot.execute(new SendMessage(chatDB.chatId(), "url " + url + " changed"));
                });
    }
}
