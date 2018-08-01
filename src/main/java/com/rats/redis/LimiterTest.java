package com.rats.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LimiterTest {

    private static JedisPool pool = null;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxTotal(200);
        // 设置最大空闲数
        config.setMaxIdle(8);
        // 设置最大等待时间
        config.setMaxWaitMillis(1000 * 100);
        // 在borrow一个jedis实例时，是否需要验证，若为true，则所有jedis实例均是可用的
        config.setTestOnBorrow(true);
        pool = new JedisPool(config, "127.0.0.1", 6379, 3000);
    }

    public static void main(String[] args){
        final RedisLimiter limiter = new RedisLimiter(pool);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for(int i =0;i<100;i++) {
            String ip = "127.0.0." + i/10;
            String reqUri = "/api/hello";
            final String x = "" + i;
            executorService.execute(()->{
                boolean ret = limiter.tryAcquire(ip,reqUri,1,10);
                System.out.println(x + "----"+ip+":" + reqUri + "---"+ ret);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });
        }

    }
}
