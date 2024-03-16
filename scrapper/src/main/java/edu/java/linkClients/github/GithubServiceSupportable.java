package edu.java.linkClients.github;

import edu.java.linkClients.SupportableLinkService;
import edu.java.linkClients.github.dto.RepositoryResponse;

public interface GithubServiceSupportable extends SupportableLinkService {
    RepositoryResponse getGithubRepository(String owner, String repo);
}
