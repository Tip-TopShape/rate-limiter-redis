local key = "SW:"..KEYS[1]
local window = tonumber(ARGV[1])
local timeStamp = tonumber(ARGV[2])
local uuid = ARGV[3]
local limit = tonumber(ARGV[4])

local allowed = 0
local cutoff = timeStamp - window
-- remove older
redis.call('ZREMRANGEBYSCORE', key, 0, cutoff)
-- count
local count = redis.call('ZCARD', key)
-- check
if count < limit then
    redis.call('ZADD', key, timeStamp, uuid)
    allowed = 1
end

local oldest = redis.call('ZRANGE', key, 0, 0, 'WITHSCORES')
local retryAfter = tonumber(oldest[2]) + window - timeStamp

return {allowed, retryAfter}