package com.rats.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisLimiter {

    private JedisPool pool;

    private static final String SCRIPT = "local key = KEYS[1] --限流KEY（一秒一个）\n" +
            "local limit_time = ARGV[1]\n" +
            "local limit_count = tonumber(ARGV[2]) --限流大小\n" +
            "local current = tonumber(redis.call('get', key) or \"0\")\n" +
            "if current + 1 > limit_count then --如果超出限流大小\n" +
            "   return 0\n" +
            "else  --请求数+1，并设置2秒过期\n" +
            "   redis.call('incr', key)\n" +
            "   redis.call('expire', key, limit_time)\n" +
            "   return 1\n" +
            "end";

    public RedisLimiter(JedisPool pool){
       this.pool = pool;
    }


    public boolean tryAcquire(String ip,String reqUri, int limit_time,int limit_count) {
        Jedis jedis = pool.getResource();
        Object result = jedis.eval(SCRIPT, 1, ip + ":" + reqUri, String.valueOf(limit_time),String.valueOf(limit_count));
        return 1 == Long.valueOf(result.toString()) ;
    }

}
