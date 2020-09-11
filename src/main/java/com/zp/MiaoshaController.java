package com.zp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * redis秒杀
 *
 * @Author zp
 * @create 2020/9/11 16:21
 */
@RestController
public class MiaoshaController {

    private static final String KEY = "kucun";

    @Autowired
    DefaultRedisScript<Boolean> redisScript;

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("/miaosha")
    public String miaosha(Integer userId) {
        if (check(userId)) {
            return "秒杀成功";
        }
        return "秒杀失败";
    }

    @GetMapping("/lua/miaosha")
    public String miaoshaByLua(Integer userId) {
        if (checkByLua(userId)) {
            return "秒杀成功";
        }
        return "秒杀失败";
    }

    @GetMapping("/test")
    public String test(String name) {
        return "hello," + name;
    }

    /**
     * 检查库存
     * 不使用lua脚本
     *
     * @return
     */
    public boolean check(Integer userId) {
        String kucun = RedisUtil.get(KEY);
        int k = Integer.parseInt(kucun);
        if (k > 0) {
            Long decr = RedisUtil.decr(KEY);
            /**
             * 虽然decr在redis中是原子操作，但是高并发最后结果还是可能出现超卖
             * 因为decr的返回值是操作后的结果，所以可以通过判断返回值是否小于0
             * 如果小于0，则无效
             */
            if (decr >= 0) {
                System.out.println("用户" + userId + "秒杀成功");
                return true;
            }
        }
        return false;
    }

    /**
     * 使用lua脚本保证查询和decr操作为一个原子操作
     * @param userId
     * @return
     */
    public boolean checkByLua(Integer userId) {
        boolean result = redisTemplate.execute(redisScript, Collections.singletonList(KEY));
        if(result){
            System.out.println("用户" + userId + "秒杀成功");
            return true;
        }
        return false;
    }
}
