package edu.java.linkClients.github;

import edu.java.exceptions.CustomWebClientException;
import edu.java.linkClients.SupportableLinkService;
import edu.java.linkClients.github.dto.IssueResponse;
import edu.java.linkClients.github.dto.PullRequestResponse;
import edu.java.linkClients.github.dto.RepositoryResponse;
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
