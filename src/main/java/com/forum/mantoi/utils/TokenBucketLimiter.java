package com.forum.mantoi.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author DELL
 */
public final class TokenBucketLimiter {

    private final long maxTokens;

    private final long refillRate;

    private AtomicInteger availableTokens;

    private volatile long lastRefillTimestamp;

    public TokenBucketLimiter(int maxTokens, int refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.availableTokens = new AtomicInteger(maxTokens);
        this.lastRefillTimestamp = System.nanoTime();
    }

    public synchronized boolean tryAcquire() {
        refill();

        if (this.availableTokens.get() > 0) {
            this.availableTokens.getAndDecrement();
        }
        return false;

    }

    private void refill() {
        long now = System.nanoTime();
        int tokenAdd = (int) (((now - lastRefillTimestamp) / 1_000_000_000) * refillRate);
        this.availableTokens = new AtomicInteger((int) Math.min(this.maxTokens, tokenAdd + this.availableTokens.get()));
        lastRefillTimestamp = now;
    }


}
