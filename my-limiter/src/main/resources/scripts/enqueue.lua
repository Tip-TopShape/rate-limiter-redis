local key = KEYS[1]
local payload = tonumber(ARGV[1])
local capacity = tonumber(ARGV[2])

local queueSize = redis.call('LLEN', key)

local successfulPush = 0
if queueSize < capacity then
    redis.call('RPUSH', key, payload)
    successfulPush = 1
end

return successfulPush

