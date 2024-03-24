package edu.java.client;

import edu.java.bot.api.UpdatesApi;
import edu.java.bot.model.LinkUpdate;
import edu.java.configuration.ApplicationConfig;
import edu.java.exception.ErrorResponse;
import edu.java.exceptions.CustomWebClientException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

@Component
@Log4j2
public class UpdatesClient implements UpdatesApi {

    private final WebClient webClient;
    private final ApplicationConfig applicationConfig;
    private final Retry retryBackoffSpec;

    @Autowired
    public UpdatesClient(ApplicationConfig applicationConfig, Retry retryBackoffSpec) {
        this.webClient = WebClient.builder()
                                  .baseUrl(applicationConfig.clientConfig()
                                                            .updateUrl())
                                  .build();
        this.retryBackoffSpec = retryBackoffSpec;
        ;
        this.applicationConfig = applicationConfig;
    }

    @Override
    public ResponseEntity<Void> updatesPost(LinkUpdate linkUpdate) {
        try {
            return webClient.post()
                            .body(Mono.just(linkUpdate), LinkUpdate.class)
                            .retrieve()

                            .toEntity(Void.class)
                            .retryWhen(retryBackoffSpec)
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
