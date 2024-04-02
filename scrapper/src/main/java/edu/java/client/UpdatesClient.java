package edu.java.client;

import edu.java.bot.api.UpdatesApi;
import edu.java.bot.model.LinkUpdate;
import edu.java.exception.ErrorResponse;
import edu.java.exceptions.CustomWebClientException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@Log4j2
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
        try {
            return webClient.post()
                            .body(Mono.just(linkUpdate), LinkUpdate.class)
                            .retrieve()
                            .toEntity(Void.class)
                            .onErrorResume(WebClientResponseException.class, Mono::error)
                            .block();
        } catch (WebClientResponseException ex) {
            ErrorResponse responseBodyAs = ex.getResponseBodyAs(ErrorResponse.class);
            log.warn(responseBodyAs);
            if (responseBodyAs != null) {
                throw new CustomWebClientException(responseBodyAs.message());
            }
            throw new CustomWebClientException(ex.getMessage());
        } catch (WebClientRequestException ex) {
            log.error(ex.getMessage());
            throw new CustomWebClientException(ex.getMessage());
        }
    }
}
