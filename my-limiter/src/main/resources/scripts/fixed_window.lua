local key = "FW:"..KEYS[1]
local window = tonumber(ARGV[1])
local limit = tonumber(ARGV[2])

local counter = redis.call('INCR', key)

local allowed = 1
if counter == 1 then
    redis.call('EXPIRE', key, window)
end

if counter > limit then 
    allowed = 0
end

local retryAfter = redis.call('TTL', key)

return {allowed, retryAfter}

