package org.V.TinL.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService{
    @Autowired
    //@Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public void setLTS(String longUrl, String shortUrl, long time) {
        redisTemplate.opsForValue().set(longUrl, shortUrl, time, TimeUnit.MINUTES);
    }

    @Override
    public void setSTL(String shortUrl, String longUrl, long time) {
        redisTemplate.opsForValue().set(shortUrl, longUrl, time, TimeUnit.MINUTES);
    }

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    @Override
    public void expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.MINUTES);
            }
            //return true;
        } catch (Exception e) {
            e.printStackTrace();
            //return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    @Override
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.MINUTES);
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    @Override
    public void set(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            //return true;
        } catch (Exception e) {
            e.printStackTrace();
            //return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    @Override
    public void set(String key, String value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
            } else {
                set(key, value);
            }
            //return true;
        } catch (Exception e) {
            e.printStackTrace();
            //return false;
        }
    }

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    @Override
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }
}
