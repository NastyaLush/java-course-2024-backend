package edu.java.bot.controller;

import edu.java.bot.api.UpdatesApi;
import edu.java.bot.model.LinkUpdate;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class UpdatesController implements UpdatesApi {
    @Override
    public ResponseEntity<Void> updatesPost(LinkUpdate linkUpdate) {
        log.info("update post linkUpdate {}", linkUpdate);
        return UpdatesApi.super.updatesPost(linkUpdate);
    }
}