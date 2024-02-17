package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.manager.UserManagerProcessorImpl;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component @Log4j2 public class BotListenerImpl implements BotListener {
    private final UserManagerProcessorImpl userManagerProcessor;
    private final TelegramBot telegramBot;

    @Autowired @Lazy public BotListenerImpl(UserManagerProcessorImpl userManagerProcessor, TelegramBot telegramBot) {
        this.userManagerProcessor = userManagerProcessor;
        this.telegramBot = telegramBot;
    }

    @Override public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        telegramBot.execute(request);

    }

    @Override public int process(List<Update> updates) {
        log.info("receive update: " + updates);
        updates.forEach(update -> {
            execute(userManagerProcessor.process(update));
        });
        return CONFIRMED_UPDATES_ALL;
    }

    @Override public void start() {
        log.info("listener starts");
    }

    @Override public void close() {
        execute(new SendMessage(1L, "the bot will come back. He need to be repaired!"));
        log.info("listener stopped");
    }
}
