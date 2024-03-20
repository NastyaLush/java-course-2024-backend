package edu.java.bot.client;

import edu.java.api.TgChatApi;
import edu.java.bot.error.ErrorResponse;
import edu.java.bot.exceptions.CustomWebClientException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@Log4j2
public class TgChatClient implements TgChatApi {
    private static final String DEFAULT_URL = "http://localhost:8080/tg-chat";
    public static final String ID = "/{id}";

    private final WebClient webClient;

    public TgChatClient() {
        this.webClient = WebClient.builder()
                                  .baseUrl(DEFAULT_URL)
                                  .build();
    }

    public TgChatClient(String baseUrl) {
        this.webClient = WebClient.builder()
                                  .baseUrl(baseUrl)
                                  .build();
    }

    @Override
    public ResponseEntity<Void> tgChatIdDelete(Long id) throws CustomWebClientException {
        try {
            return webClient.delete()
                            .uri(uriBuilder -> uriBuilder.path(ID)
                                                         .build(id))
                            .retrieve()
                            .toEntity(Void.class)
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

    @Override
    public ResponseEntity<Void> tgChatIdPost(Long id) throws CustomWebClientException {

        try {
            return webClient.post()
                            .uri(uriBuilder -> uriBuilder.path(ID)
                                                         .build(id))
                            .retrieve()
                            .toEntity(Void.class)
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
