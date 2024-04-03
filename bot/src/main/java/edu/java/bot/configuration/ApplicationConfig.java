package edu.java.bot.configuration;

import edu.java.bot.configuration.retry.BackOffType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotEmpty String telegramToken, @NotNull ClientConfig clientConfig,
                                @NotNull RetryConfig retryConfig, @NotNull String linkClientUrl,
                                @NotNull String tgChatClientUrl, @NotNull Topic topic, @NotNull Topic deadTopic) {
    public record ClientConfig(@NotNull @Min(1) Integer limit,
                               @NotNull @Min(0) Integer refillLimit,
                               @NotNull Duration delayRefill) {

    }

    public record RetryConfig(@NotNull BackOffType backOffType, @NotNull List<Integer> retryPorts,
                              @NotNull Duration delay, @Positive int maxAttempts, @Min(0) @Max(1) Double jitter) {

    }

    public record Topic(@NotNull String name, @NotNull Integer partitions, @NotNull Integer replicas) {

    }

}
