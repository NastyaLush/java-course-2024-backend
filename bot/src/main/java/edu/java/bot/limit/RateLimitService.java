package edu.java.bot.limit;

import io.github.bucket4j.Bucket;

public interface RateLimitService {
    Bucket resolveBucket(String ip);
}
