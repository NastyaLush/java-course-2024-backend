package edu.java.bot.controller;

import edu.java.bot.api.UpdatesApi;
import edu.java.bot.model.LinkUpdate;
import edu.java.bot.service.UpdatesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
public class UpdatesController implements UpdatesApi {
    private final UpdatesService updatesService;

    @Override
    public ResponseEntity<Void> updatesPost(LinkUpdate linkUpdate) {
        updatesService.updatesPost(linkUpdate);
        return ResponseEntity.ok()
                             .build();
    }
}
