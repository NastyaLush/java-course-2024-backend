package edu.java.controller;

import edu.java.api.TgChatApi;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class TgChatController implements TgChatApi {
    @Override
    public ResponseEntity<Void> tgChatIdDelete(Long id) {
        log.info("tg chat id delete {}", id);
        return TgChatApi.super.tgChatIdDelete(id);
    }

    @Override
    public ResponseEntity<Void> tgChatIdPost(Long id) {
        log.info("tg chat id post {}", id);
        return TgChatApi.super.tgChatIdPost(id);
    }
}
