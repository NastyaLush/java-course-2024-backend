package edu.java.client.linkClients.github;

import edu.java.client.linkClients.SupportableLinkService;
import edu.java.client.linkClients.github.dto.IssueResponse;
import edu.java.client.linkClients.github.dto.PullRequestResponse;
import edu.java.client.linkClients.github.dto.RepositoryResponse;
import edu.java.exception.CustomWebClientException;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public interface GithubServiceSupportable extends SupportableLinkService {
    RepositoryResponse getGithubRepository(String owner,
                                           String repo) throws CustomWebClientException;

    List<IssueResponse> getListComments(@NotNull String owner,
                                        @NotNull String repo) throws CustomWebClientException;

    List<PullRequestResponse> getListPullRequests(@NotNull String owner,
                                                  @NotNull String repo) throws CustomWebClientException;
}
