package com.glodon.glodon_netdisc.config;

/**
 * @author wuyuhan
 * @date 2023/9/1 10:57
 */
import java.util.concurrent.Semaphore;
import java.util.function.Supplier;

public class RequestLimiter {

    private final Semaphore semaphore;

    public RequestLimiter(int maxConcurrentRequests) {
        semaphore = new Semaphore(maxConcurrentRequests);
    }

    public <T> T limit(Supplier<T> supplier) throws InterruptedException {
        semaphore.acquire();
        try {
            return supplier.get();
        } finally {
            semaphore.release();
        }
    }
}

