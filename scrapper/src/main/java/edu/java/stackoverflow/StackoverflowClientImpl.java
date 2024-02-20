package edu.java.stackoverflow;

import edu.java.stackoverflow.dto.QuestionResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackoverflowClientImpl implements StackoverflowClient{
    private static final String STACKOVERFLOW_API_BASE_URL = "https://api.stackexchange.com";
    private final WebClient webClient;

    public StackoverflowClientImpl() {
        this.webClient = WebClient.builder().baseUrl(STACKOVERFLOW_API_BASE_URL).build();
    }

    public StackoverflowClientImpl(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }


    @Override
    public Mono<QuestionResponse> getQuestions(List<Integer> idList) {
        String ids = idList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));
        return webClient
                .get()
                .uri("https://api.stackexchange.com/2.3/questions/{ids}?order=desc&sort=activity&site=stackoverflow", ids)
                .retrieve()
                .bodyToMono(QuestionResponse.class);
    }

}
