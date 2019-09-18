//package org.springbootlearning.api.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;
//
///**
// * <p>缓存配置</p>
// */
//@Configuration
//public class JedisPoolCacheConfig  {
//    
//    @Value("${spring.redis.host}")
//    private String host;
//    @Value("${spring.redis.port}")
//    private int port;
//    @Value("${spring.redis.database}")
//    private int database;
//    @Value("#{'${spring.redis.timeout}'.substring(0,'${spring.redis.timeout}'.indexOf('ms'))}")
//    private int timeout;
//    @Value("${spring.redis.jedis.pool.max-idle}")
//    private int maxIdle;
//    @Value("${spring.redis.jedis.pool.min-idle}")
//    private int minIdle;
//    @Value("#{'${spring.redis.jedis.pool.max-wait}'.substring(0,'${spring.redis.jedis.pool.max-wait}'.indexOf('ms'))}")
//    private long maxWaitMillis;
//    @Value("${spring.redis.jedis.pool.max-active}")
//    private int maxActive;
//    
// 
//    @Bean
//    public JedisPool generateJedisPoolFactory() {
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxTotal(maxActive);
//        poolConfig.setMaxIdle(maxIdle);
//        poolConfig.setMinIdle(minIdle);
//        poolConfig.setMaxWaitMillis(maxWaitMillis);
//        JedisPool jedisPool = new JedisPool(poolConfig, host, port, timeout);
//        return jedisPool;
//    }
//
//}
