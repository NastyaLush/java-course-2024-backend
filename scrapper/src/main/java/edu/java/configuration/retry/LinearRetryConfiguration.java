package edu.java.configuration.retry;


import com.example.exceptions.CustomWebClientException;
import edu.java.configuration.ApplicationConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;
import static edu.java.exception.ErrorManager.filter;
import static edu.java.exception.ErrorManager.getRetryErrorMessage;

@Configuration
@Log4j2
@ConditionalOnProperty(prefix = "app", name = "retry-config.back-off-type", havingValue = "linear")
public class LinearRetryConfiguration {


    @Bean
    public Retry specifyBackOffRetry(ApplicationConfig applicationConfig) {
        return new LinearRetry(applicationConfig.retryConfig()
                                                .maxAttempts(), applicationConfig.retryConfig()
                                                                                 .delay())
                .filter(throwable -> filter(applicationConfig, throwable))
                .doBeforeRetry(retrySignal -> log.warn("do linear retry before {}", retrySignal.failure()
                                                                                               .getLocalizedMessage()))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    throw new CustomWebClientException(getRetryErrorMessage(retrySignal));
                });
    }


}
