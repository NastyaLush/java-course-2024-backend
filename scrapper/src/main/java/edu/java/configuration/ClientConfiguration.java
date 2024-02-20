package edu.java.configuration;

import edu.java.github.GithubClient;
import edu.java.github.GithubClientImpl;
import edu.java.stackoverflow.StackoverflowClient;
import edu.java.stackoverflow.StackoverflowClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Bean
    public GithubClient githubClient() {
        return new GithubClientImpl();
    }
    @Bean
    public StackoverflowClient stackOverflowClient(){
        return new StackoverflowClientImpl();
    }

}
