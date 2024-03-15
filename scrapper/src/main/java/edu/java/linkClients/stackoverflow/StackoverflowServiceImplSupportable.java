package edu.java.linkClients.stackoverflow;

import edu.java.linkClients.stackoverflow.dto.QuestionResponse;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class StackoverflowServiceImplSupportable implements StackoverflowServiceSupportable {
    public static final String DOMAIN = "stackoverflow.com";
    private static final String STACKOVERFLOW_API_BASE_URL = "https://api.stackexchange.com";
    private final WebClient webClient;

    public StackoverflowServiceImplSupportable() {
        this.webClient = WebClient.builder().baseUrl(STACKOVERFLOW_API_BASE_URL).build();
    }

    public StackoverflowServiceImplSupportable(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public Mono<QuestionResponse> getQuestions(@NotNull List<Integer> idList) {
        String ids = idList.stream().map(String::valueOf).collect(Collectors.joining(";"));

        return webClient.get().uri(uriBuilder -> uriBuilder.path("/2.3/questions/{ids}").queryParam("order", "desc")
                .queryParam("sort", "activity").queryParam("site", "stackoverflow").build(ids)).retrieve()
            .bodyToMono(QuestionResponse.class).onErrorResume(WebClientResponseException.class, Mono::error);
    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }

    @Override
    public OffsetDateTime getLastUpdateDate(String pathOfUrl) {
        String[] split = pathOfUrl.split("/");
        Mono<QuestionResponse> questions = getQuestions(List.of(Integer.getInteger(split[0])));
        return questions.block().items().getFirst().lastEditDate();
    }
}
