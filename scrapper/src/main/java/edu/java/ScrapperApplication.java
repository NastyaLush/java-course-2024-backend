package edu.java;

import edu.java.configuration.ApplicationConfig;
import edu.java.github.GithubClient;
import edu.java.github.GithubClientImpl;
import edu.java.github.dto.RepositoryResponse;
import edu.java.stackoverflow.StackoverflowClient;
import edu.java.stackoverflow.dto.QuestionResponse;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class ScrapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }

}
