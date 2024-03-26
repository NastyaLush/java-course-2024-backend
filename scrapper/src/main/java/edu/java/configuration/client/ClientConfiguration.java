package edu.java.configuration.client;

import edu.java.client.linkClients.github.GithubServiceSupportable;
import edu.java.client.linkClients.github.GithubServiceSupportableImpl;
import edu.java.client.linkClients.stackoverflow.StackoverflowServiceSupportable;
import edu.java.client.linkClients.stackoverflow.StackoverflowServiceSupportableImpl;
import edu.java.configuration.ApplicationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

@Configuration
public class ClientConfiguration {
    @Bean
    public GithubServiceSupportable githubClient(ApplicationConfig applicationConfig,
                                                 Retry retryBackoffSpec) {
        return new GithubServiceSupportableImpl(applicationConfig, retryBackoffSpec);
    }

    @Bean
    public StackoverflowServiceSupportable stackOverflowClient(ApplicationConfig applicationConfig,
                                                               Retry retryBackoffSpec) {
        return new StackoverflowServiceSupportableImpl(applicationConfig, retryBackoffSpec);
    }

}
