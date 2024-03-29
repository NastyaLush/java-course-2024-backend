package edu.java.configuration;

import edu.java.configuration.database.AccessType;
import edu.java.configuration.retry.BackOffType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull @Bean Scheduler scheduler, @NotNull @Bean RetryConfig retryConfig,
                                @NotNull @Bean ClientConfig clientConfig, @NotNull AccessType databaseAccessType,
                                @NotNull @Bean Topic topic, @NotNull Boolean useQueue) {

    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record RetryConfig(@NotNull BackOffType backOffType, @NotNull List<Integer> retryPorts,
                              @NotNull Duration delay, @Positive int maxAttempts, @Min(0) @Max(1) Double jitter) {

    }

    public record ClientConfig(@NotNull String updateUrl, @NotNull StackOverflowConfig stackOverflow,
                               @NotNull GithubConfig github, @NotNull @Min(1) Integer limit,
                               @NotNull @Min(0) Integer refillLimit,
                               @NotNull Duration delayRefill) {
        public record StackOverflowConfig(@NotNull String apiUrl, @NotNull String domain) {

        }

        public record GithubConfig(@NotNull String apiUrl, @NotNull String domain) {

        }
    }

    public record Topic(@NotNull String name, @NotNull Integer partitions, @NotNull Integer replicas) {

    }
}
