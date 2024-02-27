package edu.java.bot.client;

import edu.java.bot.api.UpdatesApi;
import edu.java.bot.model.LinkUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class UpdatesClientImpl implements UpdatesApi {
    private static final String DEFAULT_URL = "http://localhost:8090";

    private final WebClient webClient;

    public UpdatesClientImpl() {
        this.webClient = WebClient.builder().baseUrl(DEFAULT_URL).build();
    }

    public UpdatesClientImpl(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public ResponseEntity<Void> updatesPost(LinkUpdate linkUpdate) {
        return webClient.post().uri(DEFAULT_URL + "/updates").retrieve().toEntity(Void.class)
                .onErrorResume(WebClientResponseException.class, Mono::error).block();
    }
}
