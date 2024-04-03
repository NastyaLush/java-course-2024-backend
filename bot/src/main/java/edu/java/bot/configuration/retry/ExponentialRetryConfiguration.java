package edu.java.bot.configuration.retry;


import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.exceptions.CustomWebClientException;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;
import static edu.java.bot.exceptions.ErrorManager.filter;
import static edu.java.bot.exceptions.ErrorManager.getRetryErrorMessage;

@Configuration
@Log4j2
@ConditionalOnProperty(prefix = "app", name = "retry-config.back-off-type", havingValue = "exponential")
public class ExponentialRetryConfiguration {
    @Bean
    public Retry specifyBackOffRetry(ApplicationConfig applicationConfig) {
        return Retry.backoff(applicationConfig.retryConfig()
                                              .maxAttempts(), applicationConfig.retryConfig()
                                                                               .delay())
                    .filter(throwable -> filter(applicationConfig, throwable))
                    .doBeforeRetry(retrySignal -> log.warn("do exponential retry after {}",
                            retrySignal.failure()
                                       .getLocalizedMessage()))
                    .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                        throw new CustomWebClientException(getRetryErrorMessage(retrySignal));
                    })
                    .jitter(applicationConfig.retryConfig()
                                             .jitter());
    }


}