local current = redis.call('GET', KEYS[1])
return current