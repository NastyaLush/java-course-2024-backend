package edu.java.client;

import edu.java.bot.api.UpdatesApi;
import edu.java.bot.model.LinkUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class UpdatesClient implements UpdatesApi {
    private static final String DEFAULT_URL = "http://localhost:8090/updates";

    private final WebClient webClient;

    public UpdatesClient() {
        this.webClient = WebClient.builder()
                                  .baseUrl(DEFAULT_URL)
                                  .build();
    }

    public UpdatesClient(String baseUrl) {
        this.webClient = WebClient.builder()
                                  .baseUrl(baseUrl)
                                  .build();
    }

    @Override
    public ResponseEntity<Void> updatesPost(LinkUpdate linkUpdate) {
        return webClient.post()
                        .retrieve()
                        .toEntity(Void.class)
                        .onErrorResume(WebClientResponseException.class, Mono::error)
                        .block();
    }
}
