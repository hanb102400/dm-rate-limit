package com.rats.guava;

import com.google.common.util.concurrent.RateLimiter;

public class GuavaLimeterTest {
    public static void main( String[] args ) {
        RateLimiter limiter = RateLimiter.create(2);
        ConsumeService service = new ConsumeService();
        for (int i = 0; i < 50; i++) {
            ConsumeThread thread = new ConsumeThread(service,limiter);
            thread.start();
        }
    }


}
