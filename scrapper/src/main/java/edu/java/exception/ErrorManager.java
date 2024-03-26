package edu.java.exception;


import edu.java.bot.model.ApiErrorResponse;
import edu.java.configuration.ApplicationConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Log4j2
public class ErrorManager {
    private ErrorManager() {
    }

    public static String getErrorMessage(WebClientResponseException ex) {
        ApiErrorResponse responseBodyAs = ex.getResponseBodyAs(ApiErrorResponse.class);
        log.debug(responseBodyAs);
        if (responseBodyAs != null) {
            return responseBodyAs.getExceptionMessage();
        }
        return ex.getMessage();
    }

    public static String getRetryErrorMessage(Retry.RetrySignal retrySignal) {
        Throwable failure = retrySignal.failure();
        log.warn("retry failed {}", failure.getLocalizedMessage());
        if (failure instanceof WebClientResponseException) {
            return ErrorManager.getErrorMessage((WebClientResponseException) failure);
        }
        return failure.getLocalizedMessage();
    }

    public static boolean filter(ApplicationConfig applicationConfig, Throwable throwable) {
        return throwable instanceof WebClientResponseException
                && applicationConfig.retryConfig()
                                    .retryPorts()
                                    .contains(((WebClientResponseException) throwable).getStatusCode()
                                                                                      .value());
    }

}
