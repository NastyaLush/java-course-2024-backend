package edu.java.stackoverflow;

import edu.java.stackoverflow.dto.QuestionResponse;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class StackoverflowClientImpl implements StackoverflowClient {
    private static final String STACKOVERFLOW_API_BASE_URL = "https://api.stackexchange.com";
    private final WebClient webClient;

    public StackoverflowClientImpl() {
        this.webClient = WebClient.builder().baseUrl(STACKOVERFLOW_API_BASE_URL).build();
    }

    public StackoverflowClientImpl(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }


    @Override
    public Mono<QuestionResponse> getQuestions(@NotNull List<Integer> idList) {
        String ids = idList.stream().map(String::valueOf).collect(Collectors.joining(";"));

        return webClient.get().uri(uriBuilder -> uriBuilder.path("/2.3/questions/{ids}").queryParam("order", "desc")
                        .queryParam("sort", "activity").queryParam("site", "stackoverflow").build(ids)).retrieve()
                .bodyToMono(QuestionResponse.class).onErrorResume(WebClientResponseException.class, Mono::error);
    }

}
