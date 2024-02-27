package edu.java.client;

import edu.java.api.LinksApi;
import edu.java.model.AddLinkRequest;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import edu.java.model.RemoveLinkRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class LinksClient implements LinksApi {
    public static final String HEADER_NAME = "Tg-Chat-Id";
    private static final String DEFAULT_URL = "http://localhost:8080/links";

    private final WebClient webClient;

    public LinksClient() {
        this.webClient = WebClient.builder().baseUrl(DEFAULT_URL).build();
    }

    public LinksClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public ResponseEntity<LinkResponse> linksDelete(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        return webClient.method(HttpMethod.DELETE)
                .header(HEADER_NAME, String.valueOf(tgChatId))
                .body(Mono.just(removeLinkRequest), RemoveLinkRequest.class).retrieve().toEntity(LinkResponse.class)
                .onErrorResume(WebClientResponseException.class, Mono::error).block();
    }

    @Override
    public ResponseEntity<ListLinksResponse> linksGet(Long tgChatId) {
        return webClient.get()
                .header(HEADER_NAME, String.valueOf(tgChatId)).retrieve().toEntity(ListLinksResponse.class)
                .onErrorResume(WebClientResponseException.class, Mono::error).block();
    }

    @Override
    public ResponseEntity<LinkResponse> linksPost(Long tgChatId, AddLinkRequest addLinkRequest) {
        return webClient.post()
                .header(HEADER_NAME, String.valueOf(tgChatId))
                .body(Mono.just(addLinkRequest), AddLinkRequest.class).retrieve().toEntity(LinkResponse.class)
                .onErrorResume(WebClientResponseException.class, Mono::error).block();
    }
}
