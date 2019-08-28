package org.springbootlearning.api.service;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;


@Service
public class RedisService {
    
    private static final Logger LOG = LoggerFactory
            .getLogger(RedisService.class);
                    
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ValueOperations<String,String> valueOperations;

    @Autowired
    private HashOperations<String, String, String> hashOperations;

    @Autowired
    private ListOperations<String, String> listOperations;

    @Autowired
    private SetOperations<String, String> setOperations;

    @Autowired
    private ZSetOperations<String, String> zSetOperations;
    
    /**
     * 默认过期时长，单位：秒
     */
    public static final long DEFAULT_EXPIRE = 60 * 60 * 24;

    /**
     * 不设置过期时长
     */
    public static final long NOT_EXPIRE = -1;

    
    
    public StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void set(String key,String value) {
        valueOperations.set(key, value);
    }
    
    public String get(String key) {
        return valueOperations.get(key);
    }
    
    public void hset(String key,String hashKey, String value) {
        hashOperations.put(key, hashKey, value);
    }
    
    public String hget(String key,String hashKey) {
        return hashOperations.get(key, hashKey);
    }
    
    public long hincrby(String key,String hashKey,long increasement) {
        return hashOperations.increment(key, hashKey, increasement);
    }
    
    public boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 重名名key，如果newKey已经存在，则newKey的原值被覆盖
     *
     * @param oldKey
     * @param newKey
     */
    public void renameKey(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * newKey不存在时才重命名
     *
     * @param oldKey
     * @param newKey
     * @return 修改成功返回true
     */
    public boolean renameKeyNotExist(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 删除key
     *
     * @param key
     */
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 删除多个key
     *
     * @param keys
     */
    public void deleteKey(String... keys) {
        Set<String> kSet = Stream.of(keys).map(k -> k)
                .collect(Collectors.toSet());
        redisTemplate.delete(kSet);
    }

    /**
     * 删除Key的集合
     *
     * @param keys
     */
    public void deleteKey(Collection<String> keys) {
        Set<String> kSet = keys.stream().map(k -> k)
                .collect(Collectors.toSet());
        redisTemplate.delete(kSet);
    }

    /**
     * 设置key的生命周期
     *
     * @param key
     * @param time
     * @param timeUnit
     */
    public void expireKey(String key, long time, TimeUnit timeUnit) {
        redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * 指定key在指定的日期过期
     *
     * @param key
     * @param date
     */
    public void expireKeyAt(String key, Date date) {
        redisTemplate.expireAt(key, date);
    }

    /**
     * 查询key的生命周期
     *
     * @param key
     * @param timeUnit
     * @return
     */
    public long getKeyExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 将key设置为永久有效
     *
     * @param key
     */
    public void persistKey(String key) {
        redisTemplate.persist(key);
    }
    
    public long hincrbyWithExpired(String key,String hashKey,long increasement,long expiredSeconds) {
        long nowCounter = hincrby(key, hashKey, increasement);
        expireKey(key, expiredSeconds, TimeUnit.SECONDS);
        return nowCounter;
    }
    
    public long hincrbyWithExpiredInTransactionV2(String key,String hashKey,
            long increasement,long expiredSeconds) {
        Long result = redisTemplate.execute(new SessionCallback<Long>() {

            @Override
            public <K, V> Long execute(RedisOperations<K, V> operations)
                    throws DataAccessException {
                List<Object> resultList = null;
                operations.multi();
                //value will be null,becasuse command just pipelined
                Long value = operations.opsForHash().increment((K) key, hashKey, increasement);
                operations.expire((K) key, expiredSeconds, TimeUnit.SECONDS);
                resultList = operations.exec();
                if(resultList.size()<2){
                    throw new RuntimeException("operations.exec() return resultList size not match,"
                            + "expect:2,actual:"+resultList.size());
                }
                for(Object o :resultList){
                    LOG.debug("multi exec result:{}",o);
                }
                Long actualValue = Long.parseLong(String.valueOf(resultList.get(0)));
                if(Boolean.valueOf(String.valueOf(resultList.get(1)))){
                    return actualValue;
                }
                else{
                    throw new RuntimeException("operations.exec() expired return "
                            + String.valueOf(resultList.get(1)));
                }
            }
        
        });
        return result.longValue();
    }

}