package edu.java.bot.limit;


import edu.java.bot.configuration.ApplicationConfig;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimitServiceImpl implements RateLimitService {
    private final ApplicationConfig applicationConfig;
    Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    @Override
    public Bucket resolveBucket(String ip) {
        return bucketCache.computeIfAbsent(ip, this::newBucket);
    }

    private Bucket newBucket(String s) {
        Bandwidth limit = Bandwidth.classic(applicationConfig.clientConfig()
                                                             .limit(),
                Refill.greedy(applicationConfig.clientConfig()
                                               .refillLimit(),
                        applicationConfig.clientConfig()
                                         .delayRefill()));
        return Bucket.builder()
                     .addLimit(limit)
                     .build();
    }

}
