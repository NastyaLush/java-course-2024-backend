package edu.java.controller;

import edu.java.api.TgChatApi;
import edu.java.service.TgChatService;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
public class TgChatController implements TgChatApi {
    private final TgChatService tgChatService;


    @Override
    @Counted(value = "bot_massages")
    public ResponseEntity<Void> tgChatIdDelete(Long id) {
        log.info("tg chat id delete {}", id);
        tgChatService.unregister(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @Counted(value = "bot_massages")
    public ResponseEntity<Void> tgChatIdPost(Long id) {
        log.info("tg chat id post {}", id);
        tgChatService.register(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
