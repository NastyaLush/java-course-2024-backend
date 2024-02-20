package edu.java.github;

import edu.java.github.dto.RepositoryResponse;
import reactor.core.publisher.Mono;

public interface GithubClient {
    public Mono<RepositoryResponse> getGithubRepository(String owner, String repo);
}
