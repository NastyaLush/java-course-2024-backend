package edu.java.bot.service;

import edu.java.bot.bot.BotListenerImpl;
import edu.java.bot.model.LinkUpdate;
import edu.java.bot.print.Printer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class UpdatesService {
    private final BotListenerImpl botListener;
    private final Printer printer;

    public void updatesPost(LinkUpdate linkUpdate) {
        log.info("update post linkUpdate {}", linkUpdate);
        String message = "Url" + linkUpdate.getUrl() + " was changed " + linkUpdate.getDescription();
        linkUpdate.getTgChatIds()
                  .forEach(id -> botListener.execute(printer.getMessage(id, message)));
    }
}
