package edu.java.bot.configuration.retry;


import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.error.ErrorResponse;
import edu.java.bot.exceptions.CustomWebClientException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Configuration
@Log4j2
@ConditionalOnProperty(prefix = "app", name = "retry-config.back-off-type", havingValue = "linear")
public class LinearRetryConfiguration {
    @Autowired
    ApplicationConfig applicationConfig;

    @Bean
    public Retry specifyBackOffRetry(ApplicationConfig applicationConfig) {
        return new LinearRetry(applicationConfig.retryConfig()
                                                .maxAttempts(), applicationConfig.retryConfig()
                                                                                 .delay())
                .filter(throwable -> throwable instanceof WebClientResponseException && applicationConfig.retryConfig()
                                                                                                         .retryPorts()
                                                                                                         .contains(((WebClientResponseException) throwable).getStatusCode()
                                                                                                                                                           .value()))
                .doBeforeRetry(retrySignal -> log.warn("do linear retry before {}", retrySignal.failure()
                                                                                             .getLocalizedMessage()))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    Throwable failure = retrySignal.failure();
                    log.warn("retry failed {}", failure.getLocalizedMessage());
                    throw new CustomWebClientException(((WebClientResponseException) failure).getResponseBodyAs(ErrorResponse.class).message());
                });
    }
}
