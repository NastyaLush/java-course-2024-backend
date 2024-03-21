package edu.java.bot.client;

import edu.java.api.LinksApi;
import edu.java.bot.error.ErrorResponse;
import edu.java.bot.exceptions.CustomWebClientException;
import edu.java.model.AddLinkRequest;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import edu.java.model.RemoveLinkRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class LinksClient implements LinksApi {
    public static final String HEADER_NAME = "Tg-Chat-Id";
    private static final String DEFAULT_URL = "http://localhost:8080/links";

    private final WebClient webClient;

    public LinksClient() {
        this.webClient = WebClient.builder()
                                  .baseUrl(DEFAULT_URL)
                                  .build();
    }

    public LinksClient(String baseUrl) {
        this.webClient = WebClient.builder()
                                  .baseUrl(baseUrl)
                                  .build();
    }

    @Override
    public ResponseEntity<LinkResponse> linksDelete(Long tgChatId,
                                                    RemoveLinkRequest removeLinkRequest) {
        try {

            return webClient.method(HttpMethod.DELETE)
                            .header(HEADER_NAME, String.valueOf(tgChatId))
                            .body(Mono.just(removeLinkRequest), RemoveLinkRequest.class)
                            .retrieve()
                            .toEntity(LinkResponse.class)
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
    public ResponseEntity<ListLinksResponse> linksGet(Long tgChatId) {
        try {

            return webClient.get()
                            .header(HEADER_NAME, String.valueOf(tgChatId))
                            .retrieve()
                            .toEntity(ListLinksResponse.class)
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
    public ResponseEntity<LinkResponse> linksPost(Long tgChatId,
                                                  AddLinkRequest addLinkRequest) {
        try {
            return webClient.post()
                            .header(HEADER_NAME, String.valueOf(tgChatId))
                            .body(Mono.just(addLinkRequest), AddLinkRequest.class)
                            .retrieve()
                            .toEntity(LinkResponse.class)
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
