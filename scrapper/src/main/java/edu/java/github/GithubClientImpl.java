package edu.java.github;

import edu.java.github.dto.RepositoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GithubClientImpl implements GithubClient{
    private static final String GITHUB_V3_MIME_TYPE = "application/vnd.github.v3+json";
    private static final String GITHUB_API_BASE_URL = "https://api.github.com";
    private static final String USER_AGENT = "Spring 5 WebClient";

    private final WebClient webClient;


    public GithubClientImpl(String baseURL) {
        webClient = WebClient.builder().baseUrl(baseURL).build();
    }

    public GithubClientImpl() {
        webClient = WebClient.builder().baseUrl(GITHUB_API_BASE_URL).build();
    }

    @Override
    public Mono<RepositoryResponse> getGithubRepository(String owner, String repo) {
        return webClient.get()
                .uri("/repos/{owner}/{repo}", owner, repo)
                .retrieve()
                .bodyToMono(RepositoryResponse.class);
    }
}
