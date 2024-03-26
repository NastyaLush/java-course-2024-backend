package edu.java.bot.controller;

import edu.java.bot.api.UpdatesApi;
import edu.java.bot.bot.BotListenerImpl;
import edu.java.bot.model.LinkUpdate;
import edu.java.bot.print.Printer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class UpdatesController implements UpdatesApi {
    private final BotListenerImpl botListener;
    private final Printer printer;

    @Autowired
    public UpdatesController(BotListenerImpl botListener, Printer printer) {
        this.botListener = botListener;
        this.printer = printer;
    }

    @Override
    public ResponseEntity<Void> updatesPost(LinkUpdate linkUpdate) {
        log.info("update post linkUpdate {}", linkUpdate);
        String message = "Url" + linkUpdate.getUrl() + " was changed " + linkUpdate.getDescription();
        linkUpdate.getTgChatIds()
                  .forEach(id -> {
                      botListener.execute(printer.getMessage(id, message));
                  });
        return ResponseEntity.ok()
                             .build();
    }
}
