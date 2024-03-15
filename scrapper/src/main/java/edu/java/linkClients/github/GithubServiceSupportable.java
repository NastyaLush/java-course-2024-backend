package edu.java.linkClients.github;

import edu.java.linkClients.SupportableLinkService;
import edu.java.linkClients.github.dto.RepositoryResponse;
import reactor.core.publisher.Mono;

public interface GithubServiceSupportable extends SupportableLinkService {
    Mono<RepositoryResponse> getGithubRepository(String owner, String repo);
}
