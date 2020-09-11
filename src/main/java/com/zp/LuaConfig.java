package com.zp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @Author zp
 * @create 2020/9/11 17:04
 */
@Configuration
public class LuaConfig {
    @Bean
    public DefaultRedisScript<Boolean> redisScript() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/ReadAndSet.lua")));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }

}
