package edu.java.configuration;

import edu.java.linkClients.github.GithubServiceImplSupportable;
import edu.java.linkClients.github.GithubServiceSupportable;
import edu.java.linkClients.stackoverflow.StackoverflowServiceImplSupportable;
import edu.java.linkClients.stackoverflow.StackoverflowServiceSupportable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Bean
    public GithubServiceSupportable githubClient() {
        return new GithubServiceImplSupportable();
    }

    @Bean
    public StackoverflowServiceSupportable stackOverflowClient() {
        return new StackoverflowServiceImplSupportable();
    }

}
