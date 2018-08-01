package com.rats.guava;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

public class ConsumeThread  extends Thread{

    ConsumeService service ;
    RateLimiter limiter;

    public ConsumeThread( ConsumeService service,RateLimiter limiter){
        this.service = new ConsumeService();
        this.limiter = limiter;
    }

    @Override
    public void run() {
        limiter.tryAcquire(50, TimeUnit.SECONDS);
        service.consume();
    }
}
