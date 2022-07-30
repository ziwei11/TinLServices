package org.V.TinL.service;

public interface RedisService {
    void setLTS(String longUrl, String shortUrl, long time);
    void setSTL(String longUrl, String shortUrl, long time);
    void expire(String key, long time);
    long getExpire(String key);
    void set(String key, String value);
    void set(String key, String value, long time);
    Object get(String key);
}
