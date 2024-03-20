package edu.java.linkClients.github;

import edu.java.exceptions.CustomWebClientException;
import edu.java.linkClients.LinkUpdateResponse;
import edu.java.linkClients.github.dto.IssueResponse;
import edu.java.linkClients.github.dto.PullRequestResponse;
import edu.java.linkClients.github.dto.RepositoryResponse;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
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
    public RepositoryResponse getGithubRepository(@NotNull String owner,
                                                  @NotNull String repo) throws CustomWebClientException {
        try {
            return webClient.get()
                            .uri("/repos/{owner}/{repo}", owner, repo)
                            .retrieve()
                            .bodyToMono(RepositoryResponse.class)
                            .onErrorResume(WebClientResponseException.class, Mono::error)
                            .block();
        } catch (WebClientResponseException | WebClientRequestException ex) {
            log.warn(ex.getMessage());
            throw new CustomWebClientException(ex.getMessage());
        }
    }

    @Override
    public List<IssueResponse> getListComments(@NotNull String owner,
                                               @NotNull String repo) throws CustomWebClientException {
        try {
            return webClient.get()
                            .uri("/repos/{owner}/{repo}/issues/comments", owner, repo)
                            .retrieve()
                            .bodyToFlux(IssueResponse.class)
                            .onErrorResume(WebClientResponseException.class, Flux::error)
                            .collectList()
                            .block();
        } catch (WebClientResponseException | WebClientRequestException ex) {
            log.warn(ex.getMessage());
            throw new CustomWebClientException(ex.getMessage());
        }
    }

    @Override
    public List<PullRequestResponse> getListPullRequests(@NotNull String owner,
                                                         @NotNull String repo) throws CustomWebClientException {
        try {
            return webClient.get()
                            .uri("/repos/{owner}/{repo}/pulls", owner, repo)
                            .retrieve()
                            .bodyToFlux(PullRequestResponse.class)
                            .onErrorResume(WebClientResponseException.class, Flux::error)
                            .collectList()
                            .block();
        } catch (WebClientResponseException | WebClientRequestException ex) {
            log.warn(ex.getMessage());
            throw new CustomWebClientException(ex.getMessage());
        }

    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }
    //todo остальные не проверяются

    @Override
    public LinkUpdateResponse getLastUpdateDate(String pathOfUrl,
                                                OffsetDateTime lastUpdate) throws CustomWebClientException {
        OffsetDateTime newLastUpdate = lastUpdate;
        StringBuilder descriptionBuilder = new StringBuilder();
        String[] split = pathOfUrl.split("/");
        String owner = split[1];
        String repo = split[2];
        RepositoryResponse githubRepository = getGithubRepository(owner, repo);
        List<PullRequestResponse> pullRequestResponseMono = getListPullRequests(owner, repo)
                .stream()
                .filter(pullRequestResponse -> pullRequestResponse.createdAt()
                                                                  .isAfter(lastUpdate))
                .toList();
        List<IssueResponse> listComments = getListComments(
                owner,
                repo
        ).stream()
         .filter(issueResponse -> issueResponse.createdAt()
                                               .isAfter(lastUpdate))
         .toList();
        String repositoryBeginning = "Repository ";
        if (githubRepository.updatedAt()
                            .isAfter(lastUpdate)) {
            newLastUpdate = githubRepository.updatedAt();
            descriptionBuilder.append(repositoryBeginning)
                              .append(githubRepository.name())
                              .append(" was changed\n");
        }
        if (!pullRequestResponseMono.isEmpty()) {
            for (PullRequestResponse pullRequestResponse : pullRequestResponseMono) {
                newLastUpdate = newLastUpdate.isAfter(pullRequestResponse.createdAt()) ? newLastUpdate
                        : pullRequestResponse.createdAt();
                descriptionBuilder.append(repositoryBeginning)
                                  .append(githubRepository.name())
                                  .append(" has new pull request.\n")
                                  .append("link: ")
                                  .append(pullRequestResponse.url())
                                  .append("\ntitle")
                                  .append(pullRequestResponse.title())
                                  .append("\nbody")
                                  .append(pullRequestResponse.body())
                                  .append("\ncreated by")
                                  .append(pullRequestResponse.userResponse()
                                                             .login())
                                  .append(" ")
                                  .append(pullRequestResponse.userResponse()
                                                             .url())
                                  .append("\n");
            }
        }
        if (!listComments.isEmpty()) {
            for (IssueResponse listComment : listComments) {
                newLastUpdate =
                        newLastUpdate.isAfter(listComment.createdAt()) ? newLastUpdate : listComment.createdAt();
                descriptionBuilder.append(repositoryBeginning)
                                  .append(githubRepository.name())
                                  .append(" has new commit.\n")
                                  .append("link to commit: ")
                                  .append(listComment.htmlUtl())
                                  .append("\n");
            }
        }

        return new LinkUpdateResponse(newLastUpdate, descriptionBuilder.toString());
    }
}
