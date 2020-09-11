local current = redis.call('GET', KEYS[1])
if tonumber(current) > 0
  then redis.call('DECR', KEYS[1])
  return true
end
return false