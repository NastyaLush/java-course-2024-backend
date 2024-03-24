package edu.java.bot.configuration.retry;

import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Log4j2
public class LinearRetry extends Retry {
    private final int maxRetries;
    private final Duration defaultBackoff;
    public Predicate<? super Throwable> errorFilter;
    public Consumer<RetrySignal> doBeforeRetry;
    public BiFunction<LinearRetry, RetrySignal, Throwable> retryExhaustedGenerator;
    static final Consumer<RetrySignal> NO_OP_CONSUMER = (rs) -> {
    };

    public LinearRetry(int maxRetries, Duration duration) {
        this.maxRetries = maxRetries;
        this.defaultBackoff = duration;
    }

    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> retrySignals) {
        return retrySignals.flatMap(this::getRetry);
    }

    private Mono<Long> getRetry(RetrySignal rs) {
        log.info("do retry");
        RetrySignal copy = rs.copy();
        if (doBeforeRetry != NO_OP_CONSUMER) {
            try {
                doBeforeRetry.accept(copy);
            } catch (Throwable var10) {
                return Mono.error(var10);
            }
        }
        if (!this.errorFilter.test(rs.failure())) {
            return Mono.error(rs.failure());
        }
        if (rs.totalRetries() < maxRetries) {
            Duration delay = defaultBackoff.multipliedBy(rs.totalRetries());

            log.info("retry {} with backoff {}sec", rs.totalRetries(), delay.toSeconds());
            return Mono.delay(delay)
                       .thenReturn(rs.totalRetries());
        } else {
            return Mono.error(this.retryExhaustedGenerator.apply(this, copy));
        }

    }

    public LinearRetry filter(Predicate<? super Throwable> errorFilter) {
        this.errorFilter = errorFilter;
        return this;
    }

    public LinearRetry doBeforeRetry(Consumer<RetrySignal> doBeforeRetry) {
        this.doBeforeRetry = doBeforeRetry;
        return this;
    }

    public LinearRetry onRetryExhaustedThrow(BiFunction<LinearRetry, RetrySignal, Throwable> retryExhaustedGenerator) {
        this.retryExhaustedGenerator = retryExhaustedGenerator;
        return this;
    }
}