package edu.java.controller;

import edu.java.api.LinksApi;
import edu.java.model.AddLinkRequest;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import edu.java.model.RemoveLinkRequest;
import edu.java.service.UrlService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController

public class LinksController implements LinksApi {
    private final UrlService urlService;

    @Autowired
    public LinksController(UrlService urlService) {
        this.urlService = urlService;
    }

    @Override
    public ResponseEntity<LinkResponse> linksDelete(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        log.info("links delete tgChatId {} removeLinkRequest {}", tgChatId, removeLinkRequest);
        LinkResponse linkResponse = urlService.remove(tgChatId, removeLinkRequest.getLink());
        return new ResponseEntity<>(linkResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ListLinksResponse> linksGet(Long tgChatId) {
        log.info("links get tgChatId {}", tgChatId);
        ListLinksResponse listLinksResponse = urlService.listAll(tgChatId);
        return new ResponseEntity<>(listLinksResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LinkResponse> linksPost(Long tgChatId, AddLinkRequest addLinkRequest) {
        log.info("links post tgChatId {} addLinkRequest {}", tgChatId, addLinkRequest);
        LinkResponse linkResponse = urlService.add(tgChatId, addLinkRequest.getLink());
        return new ResponseEntity<>(linkResponse, HttpStatus.OK);
    }
}
