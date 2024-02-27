package edu.java.controller;

import edu.java.api.LinksApi;
import edu.java.model.AddLinkRequest;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import edu.java.model.RemoveLinkRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class LinksController implements LinksApi {
    @Override
    public ResponseEntity<LinkResponse> linksDelete(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        log.info("links delete tgChatId {} removeLinkRequest {}", tgChatId, removeLinkRequest);
        return LinksApi.super.linksDelete(tgChatId, removeLinkRequest);
    }

    @Override
    public ResponseEntity<ListLinksResponse> linksGet(Long tgChatId) {
        log.info("links get tgChatId {}", tgChatId);
        return LinksApi.super.linksGet(tgChatId);
    }

    @Override
    public ResponseEntity<LinkResponse> linksPost(Long tgChatId, AddLinkRequest addLinkRequest) {
        log.info("links post tgChatId {} addLinkRequest {}", tgChatId,addLinkRequest);
        return LinksApi.super.linksPost(tgChatId, addLinkRequest);
    }
}
