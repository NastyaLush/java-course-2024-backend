package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.TgChatClient;
import edu.java.bot.exceptions.WebClientException;
import edu.java.bot.print.Printer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class CommandStart implements Command {
    public static final String ERROR_MESSAGE = "There is an error occurred\n";
    public static final int HTTP_OK_STATUS = 200;
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
        ResponseEntity<Void> voidResponseEntity;
        try {
            voidResponseEntity = tgChatClient.tgChatIdPost(id);
        } catch (WebClientException e) {
            log.error(e);
            return printer.getMessage(
                    id,
                    ERROR_MESSAGE + printer.makeBold(e.getMessage())
            );
        }
        if (voidResponseEntity.getStatusCode() == HttpStatusCode.valueOf(HTTP_OK_STATUS)) {
            return printer.getMessage(
                    id,
                    "Welcome to our bot and congratulations, you were successfully registered");
        } else {
            return printer.getMessage(
                    id,
                    ERROR_MESSAGE + printer.makeBold(voidResponseEntity.getStatusCode()
                                                                       .toString())
            );
        }
    }
}
