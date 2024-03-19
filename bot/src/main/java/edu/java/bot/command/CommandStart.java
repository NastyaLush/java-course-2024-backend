package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.LinksClient;
import edu.java.bot.client.TgChatClient;
import edu.java.bot.print.Printer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandStart implements Command {
    private final TgChatClient tgChatClient;

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "register";
    }

    @Override
    public SendMessage handle(Update update, Printer printer) {
        Long id = update.message()
                        .chat()
                        .id();
        ResponseEntity<Void> voidResponseEntity = tgChatClient.tgChatIdPost(id);
        if(voidResponseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            return printer.getMessage(
                    id,
                    "welcome to our bot and congratulations, you were successfully registered");
        } else{
            return printer.getMessage(
                    id,
                    "There is an error occurred " + voidResponseEntity.getStatusCode()
            );
        }
    }
}
