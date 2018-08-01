-[[
ip：客户端ip地址      KEYS[1]
limit_time：访问时间段    ARGV[1]
limit_count:在这个时间内限制访问多少次   ARGV[2]
]]
local key = KEYS[1] --限流KEY（一秒一个）
local limit_time = ARGV[1]
local limit_count = tonumber(ARGV[2]) --限流大小
local current = tonumber(redis.call('get', key) or "0")
if current + 1 > limit_count then --如果超出限流大小
   return 0
else  --请求数+1，并设置 limit_time 秒过期
   redis.call('incr', key)
   redis.call('expire', key, limit_time)
   return 1
end