package edu.java.github;

import edu.java.github.dto.RepositoryResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class GithubClientImpl implements GithubClient {
    private static final String GITHUB_API_BASE_URL = "https://api.github.com";

    private final WebClient webClient;

    public GithubClientImpl(String baseURL) {
        webClient = WebClient.builder().baseUrl(baseURL).build();
    }

    public GithubClientImpl() {
        webClient = WebClient.builder().baseUrl(GITHUB_API_BASE_URL).build();
    }

    @Override
    public Mono<RepositoryResponse> getGithubRepository(@NotNull String owner, @NotNull String repo) {
        return webClient.get()
                .uri("/repos/{owner}/{repo}", owner, repo)
                .retrieve()
                .bodyToMono(RepositoryResponse.class).onErrorResume(WebClientResponseException.class, Mono::error);
    }
}
