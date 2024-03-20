package edu.java.linkClients.github;

import edu.java.exceptions.WebClientException;
import edu.java.linkClients.SupportableLinkService;
import edu.java.linkClients.github.dto.IssueResponse;
import edu.java.linkClients.github.dto.PullRequestResponse;
import edu.java.linkClients.github.dto.RepositoryResponse;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public interface GithubServiceSupportable extends SupportableLinkService {
    RepositoryResponse getGithubRepository(String owner, String repo) throws WebClientException;

    List<IssueResponse> getListComments(@NotNull String owner, @NotNull String repo) throws WebClientException;

    List<PullRequestResponse> getListPullRequests(@NotNull String owner, @NotNull String repo) throws WebClientException;
}
