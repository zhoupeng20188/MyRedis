package com.zp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author zp
 * @create 2019/10/23 14:44
 */
@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static RedisUtil redisUtil;

    @PostConstruct
    public void init() {
        // restTemplate和redisTemplate为static时无法注入，必须用redisUtil.redisTemplate形式
        redisUtil = this;
    }

    /**
     * 设置key
     *
     * @param key    key值
     * @param value  value值
     * @param expire 过期时间
     * @param timeUnit 时间单位
     */
    public static void setKey(String key, String value, int expire, TimeUnit timeUnit) {
        if (!StringUtils.isEmpty(key)) {
            redisUtil.stringRedisTemplate.opsForValue().set(key, value, expire, timeUnit);
        }
    }

    /**
     * 获取key值
     * @param key key
     * @return key值
     */
    public static String get(String key){
        return redisUtil.stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 判断key是否存在
     *
     * @param key key值
     * @return true:存在，false:不存在
     */
    public static boolean isExists(String key) {
        return redisUtil.stringRedisTemplate.hasKey(key);
    }

    /**
     *
     * Title: remove
     * Description: 移除数据
     * @param key 支持模糊匹配,例如：*test*
     */
    public static void remove(String key) {
        // stringRedisTemplate匹配可以用*test*,匹配方法与命令行里一样
        // redisTemplate的匹配有问题，如果存入的key为test1，匹配test*可以，但匹配tes*结果为空。必须后面有多少位就写多少个*
        // stringRedisTemplate和redisTemplate管理的数据是不通用的
        // 所以选用stringRedisTemplate，但是所以选用stringRedisTemplate存的数据只能是string
        Set<String> keys = redisUtil.stringRedisTemplate.keys(key);
        if (!CollectionUtils.isEmpty(keys)) {
            redisUtil.stringRedisTemplate.delete(keys);
        }
    }

    /**
     * -1操作
     * @param key
     */
    public static Long  decr(String  key){
        Long decrement = redisUtil.stringRedisTemplate.opsForValue().decrement(key);
        return decrement;
    }
}
