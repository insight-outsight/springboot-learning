package org.springbootlearning.api.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class CacheService<T> {

    private final Logger logger = LoggerFactory.getLogger(CacheService.class);

    private String name;
    private long expiredSeconds;
    private long maximumSize;

    private final Cache<String, T> caches;

    public CacheService(String name,long expiredSeconds,
            int initialCapacity,int maximumSize) {
        if(expiredSeconds < 1 || initialCapacity < 10 || maximumSize > 1000000
                || initialCapacity > 1000000 || maximumSize < 10 
                || initialCapacity > maximumSize) {
            throw new IllegalArgumentException("expiredSeconds:"+expiredSeconds
                    +",initialCapacity:"+initialCapacity
                    +",maximumSize:"+maximumSize);
        }
        this.name = name;
        this.expiredSeconds = expiredSeconds;
        this.maximumSize = maximumSize;
        caches = Caffeine.newBuilder()
                .softValues()
                .expireAfterWrite(expiredSeconds, TimeUnit.SECONDS)
                .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .recordStats()
                .build();
    }
    
    public Cache<String, T> getCaches() {
        return caches;
    }
    
    public T get(String key) {
        return caches.getIfPresent(key);
    }
    
    public void set(String key,T value) {
        caches.put(key, value);
    }
    
    public void invalidate(String key) {
        caches.invalidate(key);
    }

    @PostConstruct
    public void printStatInfo() {
        Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {

            @Override
            public Thread newThread(Runnable arg0) {
                return new Thread(arg0, "caffeineCache-stat-thread");
            }

        }).scheduleWithFixedDelay(new Runnable() {

            @Override
            public void run() {
                logger.info("--------["+name+"] caches maximumSize:"+ maximumSize+",usedSize:"+caches.estimatedSize()
                        + ",expiredSeconds:" + expiredSeconds
                        +",cacheHitCount:"+ caches.stats().hitCount()
                        + ",cacheHitRatio:" + caches.stats().hitRate()
                        + "--------");
            }

        }, 2, 30, TimeUnit.SECONDS);
    }

    
}
