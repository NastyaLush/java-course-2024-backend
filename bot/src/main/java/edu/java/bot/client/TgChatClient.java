package edu.java.bot.client;

import com.example.exceptions.CustomWebClientException;
import edu.java.api.TgChatApi;
import edu.java.bot.configuration.ApplicationConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;
import static edu.java.bot.exceptions.ErrorManager.getErrorMessage;

@Component
@Log4j2
public class TgChatClient implements TgChatApi {
    public static final String ID = "/{id}";
    private final Retry retryBackoffSpec;

    private final WebClient webClient;

    @Autowired

    public TgChatClient(Retry retryBackoffSpec, ApplicationConfig applicationConfig) {
        this.retryBackoffSpec = retryBackoffSpec;
        this.webClient = WebClient.builder()
                                  .baseUrl(applicationConfig.tgChatClientUrl())
                                  .build();
    }


    @Override
    public ResponseEntity<Void> tgChatIdDelete(Long id) {
        try {
            return webClient.delete()
                            .uri(uriBuilder -> uriBuilder.path(ID)
                                                         .build(id))
                            .retrieve()
                            .toEntity(Void.class)
                            .retryWhen(retryBackoffSpec)
                            .block();
        } catch (WebClientResponseException ex) {
            throw new CustomWebClientException(getErrorMessage(ex));
        } catch (WebClientRequestException ex) {
            log.error(ex.getMessage());
            throw new CustomWebClientException(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<Void> tgChatIdPost(Long id) {

        try {
            return webClient.post()
                            .uri(uriBuilder -> uriBuilder.path(ID)
                                                         .build(id))
                            .retrieve()
                            .toEntity(Void.class)
                            .retryWhen(retryBackoffSpec)
                            .block();
        } catch (WebClientResponseException ex) {
            throw new CustomWebClientException(getErrorMessage(ex));
        } catch (WebClientRequestException ex) {
            log.error(ex.getMessage());
            throw new CustomWebClientException(ex.getMessage());
        }
    }
}
