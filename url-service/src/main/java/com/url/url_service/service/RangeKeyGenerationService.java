package com.url.url_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class RangeKeyGenerationService {

    @Autowired
    @Qualifier(value = "counterTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    private static final String ID_COUNTER_KEY = "url_shortener:global_id";
    private static final long BATCH_SIZE = 1000; // Give each server 1000 IDs at a time

    private AtomicLong currentId = new AtomicLong(0);
    private volatile long maxId = 0;
    public synchronized long generateUniqueId() {
        // If we exhausted our allocated range (or it's the first run)
        if (currentId.get() >= maxId) {
            fetchNewRangeFromRedis();
        }
        return currentId.incrementAndGet();
    }
    private void fetchNewRangeFromRedis() {
        // Increment by BATCH_SIZE.
        // Example: If it was at 5000, it becomes 6000 and returns 6000.
        Long newMax = redisTemplate.opsForValue().increment(ID_COUNTER_KEY, BATCH_SIZE);

        if (newMax == null) {
            throw new RuntimeException("Failed to fetch ID range from Redis");
        }

        // Update our in-memory range limits
        // If Redis returned 6000, our range is 5001 to 6000.
        this.maxId = newMax;
        this.currentId.set(newMax - BATCH_SIZE);
    }
}
