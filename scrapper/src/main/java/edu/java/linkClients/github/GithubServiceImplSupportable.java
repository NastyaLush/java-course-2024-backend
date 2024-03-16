package edu.java.linkClients.github;

import edu.java.linkClients.LinkUpdateResponse;
import edu.java.linkClients.github.dto.IssueResponse;
import edu.java.linkClients.github.dto.PullRequestResponse;
import edu.java.linkClients.github.dto.RepositoryResponse;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class GithubServiceImplSupportable implements GithubServiceSupportable {
    public static final String DOMAIN = "github.com";
    private static final String GITHUB_API_BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    public GithubServiceImplSupportable(String baseURL) {
        webClient = WebClient.builder().baseUrl(baseURL).build();
    }

    public GithubServiceImplSupportable() {
        webClient = WebClient.builder().baseUrl(GITHUB_API_BASE_URL).build();
    }

    @Override
    public RepositoryResponse getGithubRepository(@NotNull String owner, @NotNull String repo) {
        return webClient.get()
            .uri("/repos/{owner}/{repo}", owner, repo)
            .retrieve()
            .bodyToMono(RepositoryResponse.class).onErrorResume(WebClientResponseException.class, Mono::error).block();
    }

    public List<IssueResponse> getListComments(@NotNull String owner, @NotNull String repo) {
        return webClient.get()
            .uri( "/repos/{owner}/{repo}/issues/comments", owner, repo)
            .retrieve()
            .bodyToFlux(IssueResponse.class).onErrorResume(WebClientResponseException.class, Flux::error).collectList()
            .block();
    }

    public List<PullRequestResponse> getListPullRequests(@NotNull String owner, @NotNull String repo) {
        return webClient.get()
            .uri("/repos/{owner}/{repo}/pulls", owner, repo)
            .retrieve()
            .bodyToFlux(PullRequestResponse.class).onErrorResume(WebClientResponseException.class, Flux::error)
            .collectList().block();

    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }

    @Override
    public Optional<LinkUpdateResponse> getLastUpdateDate(String pathOfUrl, OffsetDateTime lastUpdate) {
        OffsetDateTime newLastUpdate = lastUpdate;
        StringBuilder descriptionBuilder = new StringBuilder();
        String[] split = pathOfUrl.split("/");
        RepositoryResponse githubRepository = getGithubRepository(split[1], split[2]);
        List<PullRequestResponse> pullRequestResponseMono = getListPullRequests(split[0], split[1]).stream()
            .filter(pullRequestResponse -> pullRequestResponse.createdAt().isAfter(lastUpdate)).toList();
        List<IssueResponse> listComments = getListComments(split[0], split[1]).stream().filter(
            issueResponse -> issueResponse.createdAt().isAfter(lastUpdate)
        ).toList();
        if (githubRepository.updatedAt().isAfter(lastUpdate)) {
            newLastUpdate = githubRepository.updatedAt();
            descriptionBuilder.append("Repository ").append(githubRepository.name()).append(" was changed\n");
        }
        if (!pullRequestResponseMono.isEmpty()) {
            for (PullRequestResponse pullRequestResponse : pullRequestResponseMono) {
                newLastUpdate = newLastUpdate.isAfter(pullRequestResponse.createdAt()) ? newLastUpdate
                    : pullRequestResponse.createdAt();
                descriptionBuilder.append("Repository ").append(githubRepository.name())
                    .append(" has new pull request.\n").append("link: ").append(pullRequestResponse.url())
                    .append("\ntitle").append(pullRequestResponse.title()).append("\nbody")
                    .append(pullRequestResponse.body()).append("\ncreated by")
                    .append(pullRequestResponse.userResponse().login()).append(" ")
                    .append(pullRequestResponse.userResponse().url()).append("\n");
            }
        }
        if (!listComments.isEmpty()) {
            for (IssueResponse listComment : listComments) {
                newLastUpdate = newLastUpdate.isAfter(listComment.createdAt()) ? newLastUpdate
                    : listComment.createdAt();
                descriptionBuilder.append("Repository ").append(githubRepository.name()).append(" has new commit.\n")
                    .append("link: ").append(listComment.htmlUtl()).append("\n");
            }
        }
        if (descriptionBuilder.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new LinkUpdateResponse(lastUpdate, descriptionBuilder.toString()));
    }
}
