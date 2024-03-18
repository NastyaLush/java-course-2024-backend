package edu.java.linkClients.github;

import edu.java.linkClients.github.dto.RepositoryResponse;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Objects;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class GithubServiceImplSupportable implements GithubServiceSupportable {
    public static final String DOMAIN = "github.com";
    private static final String GITHUB_API_BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    public GithubServiceImplSupportable(String baseURL) {
        webClient = WebClient.builder()
                             .baseUrl(baseURL)
                             .build();
    }

    public GithubServiceImplSupportable() {
        webClient = WebClient.builder()
                             .baseUrl(GITHUB_API_BASE_URL)
                             .build();
    }

    @Override
    public Mono<RepositoryResponse> getGithubRepository(@NotNull String owner, @NotNull String repo) {
        return webClient.get()
                        .uri("/repos/{owner}/{repo}", owner, repo)
                        .retrieve()
                        .bodyToMono(RepositoryResponse.class)
                        .onErrorResume(WebClientResponseException.class, Mono::error);
    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }

    @Override
    public OffsetDateTime getLastUpdateDate(String pathOfUrl) {
        String[] split = pathOfUrl.split("/");
        Mono<RepositoryResponse> githubRepository = getGithubRepository(split[0], split[1]);
        return Objects.requireNonNull(githubRepository.block())
                      .updatedAt();
    }
}
