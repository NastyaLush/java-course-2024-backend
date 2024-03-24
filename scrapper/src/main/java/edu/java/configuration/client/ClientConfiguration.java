package edu.java.configuration.client;

import edu.java.configuration.ApplicationConfig;
import edu.java.linkClients.github.GithubServiceImplSupportable;
import edu.java.linkClients.github.GithubServiceSupportable;
import edu.java.linkClients.stackoverflow.StackoverflowServiceImplSupportable;
import edu.java.linkClients.stackoverflow.StackoverflowServiceSupportable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

@Configuration
public class ClientConfiguration {
    @Bean
    public GithubServiceSupportable githubClient(ApplicationConfig applicationConfig,
                                                 Retry retryBackoffSpec) {
        return new GithubServiceImplSupportable(applicationConfig, retryBackoffSpec);
    }

    @Bean
    public StackoverflowServiceSupportable stackOverflowClient(ApplicationConfig applicationConfig,
                                                               Retry retryBackoffSpec) {
        return new StackoverflowServiceImplSupportable(applicationConfig, retryBackoffSpec);
    }

}
