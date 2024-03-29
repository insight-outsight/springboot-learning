package org.springbootlearning.api.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;

import redis.clients.jedis.JedisPoolConfig;

import org.springbootlearning.api.service.CacheService;
import org.springbootlearning.api.service.es.Counter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>缓存配置</p>
 */
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
 
    public static final int DEFAULT_MAXSIZE = 10000;
    public static final int DEFAULT_TTL = 600;

    @Bean
    public CacheService<String> testStrCacheService() {
        return new CacheService<String>("testStrCac",40*60*60,100,1000);
    }
    
    @Bean
    public CacheService<Counter> testCounterCacheService() {
        return new CacheService<Counter>("testCntCac",60,100,100000);
    }
    
    public enum Caches {
        /**
         * 获取存在加工厂的城市
         **/
        companyAreaInfo(1800, 10),
        /**
         * 根据areaCode获得地区名
         **/
        getAreaNameByCode(604800, 1500),
        ;

        Caches() {
        }

        Caches(int ttl) {
            this.ttl = ttl;
        }

        Caches(int ttl, int maxSize) {
            this.ttl = ttl;
            this.maxSize = maxSize;
        }

        private int maxSize = DEFAULT_MAXSIZE;
        private int ttl = DEFAULT_TTL;

        public int getMaxSize() {
            return maxSize;
        }

        public int getTtl() {
            return ttl;
        }
    }

    /**
     * 创建基于Caffeine的Cache Manager
     */
    @Bean("caffeineCacheManager")
    @Primary
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        ArrayList<CaffeineCache> caches = new ArrayList<>();
        for (Caches c : Caches.values()) {
            caches.add(new CaffeineCache(c.name(),
                    Caffeine.newBuilder().recordStats()
                            .expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
                            .maximumSize(c.getMaxSize())
                            .build())
            );
        }
        /**
         * Caffeine是一个高性能Java 缓存库，使用Java8对Guava缓存重写版本，在Spring Boot 2.0中将取代Guava。 
            使用spring.cache.cache-names属性可以在启动时创建缓存 
            例如，以下application配置创建一个foo和bar缓存，最大数量为500，存活时间为10分钟
            
            spring.cache.cache-names=foo,bar
            spring.cache.caffeine.spec=maximumSize=500,expireAfterAccess=600s
            
            or
            
            spring:
              cache:
                cache-names: outLimit，notOutLimit
                caffeine:
                  spec: initialCapacity=50,maximumSize=500,expireAfterWrite=5s,refreshAfterWrite=7s
                type: caffeine

            此处使用的是编程配置方式，

         * Caffeine配置说明： 
            initialCapacity=[integer]: 初始的缓存空间大小 
            maximumSize=[long]: 缓存的最大条数 
            maximumWeight=[long]: 缓存的最大权重 
            expireAfterAccess=[duration]: 最后一次写入或访问后经过固定时间过期 
            expireAfterWrite=[duration]: 最后一次写入后经过固定时间过期 
            refreshAfterWrite=[duration]: 创建缓存或者最近一次更新缓存后经过固定的时间间隔，刷新缓存 
            weakKeys: 打开key的弱引用
            weakValues：打开value的弱引用
            softValues：打开value的软引用
            recordStats：开发统计功能
            
            注意： 
            expireAfterWrite和expireAfterAccess同时存在时，以expireAfterWrite为准。 
            maximumSize和maximumWeight不可以同时使用
         */
        cacheManager.setCaches(caches);
        return cacheManager;
    }
    
    @Bean
    public KeyGenerator KeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }
 
    @Bean("redisCacheManager")
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory,
            @Qualifier("customCommonJackson2JsonRedisSerializer") Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer) {
        
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();  // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
        
        config = config.entryTtl(Duration.ofMinutes(10))     // 设置缓存的默认过期时间，也是使用Duration设置
                /*.disableCachingNullValues()*/;     // 不缓存空值
        
        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = customCommonJackson2JsonRedisSerializer();

        // 设置一个初始化的缓存空间set集合
        Set<String> cacheNames =  new HashSet<>();
        cacheNames.add("userCache");

        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
                
        configMap.put("userCache", newRedisCacheConfiguration(config,stringRedisSerializer,jackson2JsonRedisSerializer,30*24*60*60));

        RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)     // 使用自定义的缓存配置初始化一个cacheManager
                .initialCacheNames(cacheNames)  // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
                .withInitialCacheConfigurations(configMap)
                .build();
        return cacheManager;
        
    }

    private  RedisCacheConfiguration newRedisCacheConfiguration(RedisCacheConfiguration config,RedisSerializer<String> keySerializer,
            RedisSerializer<?> valueSerializer ,long expiredSeconds){
        return config.entryTtl(Duration.ofSeconds(expiredSeconds))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer));
    }
    
//    @Bean
//    public Jackson2JsonRedisSerializer<Object> getCustomCommonJackson2JsonRedisSerializer() {
//        
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        //指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        // 指定序列化输入的类型，类必须是非final修饰的
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//        return jackson2JsonRedisSerializer;
//        
//    }
//    
//    @Value("${spring.redis.host}")
//    private String host;
//    @Value("${spring.redis.port}")
//    private int port;
//    @Value("${spring.redis.database}")
//    private int database;
//    @Value("#{'${spring.redis.timeout}'.substring(0,'${spring.redis.timeout}'.indexOf('ms'))}")
//    private long timeout;
//    @Value("${spring.redis.jedis.pool.max-idle}")
//    private int maxIdle;
//    @Value("${spring.redis.jedis.pool.min-idle}")
//    private int minIdle;
//    @Value("#{'${spring.redis.jedis.pool.max-wait}'.substring(0,'${spring.redis.jedis.pool.max-wait}'.indexOf('ms'))}")
//    private long maxWaitMillis;
//    @Value("${spring.redis.jedis.pool.max-active}")
//    private int maxActive;
//    
//    @Bean
//    @Primary
//    public RedisConnectionFactory connectionFactory() {
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMaxTotal(maxActive);
//        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
//        jedisPoolConfig.setMaxIdle(maxIdle);
//        jedisPoolConfig.setMinIdle(minIdle);
//
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//        redisStandaloneConfiguration.setDatabase(database);
//        redisStandaloneConfiguration.setHostName(host);
//        redisStandaloneConfiguration.setPort(port);
////        RedisPassword password = RedisPassword.of("123456");
////        configuration.setPassword(password);
////        //获得默认的连接池构造
////        //这里需要注意的是，jedisConnectionFactoryJ对于Standalone模式的没有（RedisStandaloneConfiguration，JedisPoolConfig）的构造函数，对此
////        //我们用JedisClientConfiguration接口的builder方法实例化一个构造器，还得类型转换
////        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcf = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
////        //修改我们的连接池配置
////        jpcf.poolConfig(jedisPoolConfig);
////        //通过构造器来构造jedis客户端配置
////        JedisClientConfiguration jedisClientConfiguration = jpcf.build();
//
//        JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();
//        builder.usePooling().poolConfig(jedisPoolConfig).and().readTimeout(Duration.ofMillis(timeout));
//        /**
//         * 
//        if (spring.redis.ssl=false){
//            jedisClientConfiguration = JedisClientConfiguration.builder().usePooling().
//                    poolConfig(poolConfig).and().
//                    readTimeout(Duration.ofMillis(spring.redis.connTimeout=5000ms)).useSsl()
//                    .build();
//        }else {
//            jedisClientConfiguration = JedisClientConfiguration.builder().usePooling().
//                    poolConfig(poolConfig).and().
//                    readTimeout(Duration.ofMillis(redisProperties.getConnTimeout())).build();
//        }
//         */
//        JedisClientConfiguration jedisClientConfiguration = builder.build();
//        
//        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
//    }
//    
////    @Bean
////    public JedisPool generateJedisPoolFactory() {
////        JedisPoolConfig poolConfig = new JedisPoolConfig();
////        poolConfig.setMaxTotal(maxActive);
////        poolConfig.setMaxIdle(maxIdle);
////        poolConfig.setMinIdle(minIdle);
////        poolConfig.setMaxWaitMillis(maxWaitMillis);
////        JedisPool jedisPool = new JedisPool(poolConfig, host, port, timeout, password);
////        return jedisPool;
////    }
//    
//    
//    @Bean("redisTemplate")
//    public RedisTemplate<String, String> stringRedisTemplate(RedisConnectionFactory connectionFactory) {
//        
//        return new StringRedisTemplate(connectionFactory);
//        
//    }
//    
//    @Bean("tRedisTemplate")
//    public <T> RedisTemplate<String, T> redisTemplateWithValueJackson2JsonSerializer(RedisConnectionFactory connectionFactory) {
//        
//        RedisTemplate<String, T> template = new RedisTemplate<String, T>();
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =  getCustomCommonJackson2JsonRedisSerializer() ;
//        
//        //使用StringRedisSerializer来序列化和反序列化redis的key
//        template.setKeySerializer(new StringRedisSerializer());
//        //使用StringRedisSerializer来序列化和反序列化redis的value
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        
//        // 设置hash key 和value序列化模式
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//        
//        template.setConnectionFactory(connectionFactory);
//        template.afterPropertiesSet();
//        return template;
//        
//    }
//
//    /**
//     * 对redis字符串类型数据操作
//     *
//     * @param redisTemplate
//     * @return
//     */
//    @Bean
//    public ValueOperations<String, String> valueOperations(RedisTemplate<String, String> redisTemplate) {
//        return redisTemplate.opsForValue();
//    }
//    
//    /**
//     * 对hash类型的数据操作
//     *
//     * @param redisTemplate
//     * @return
//     */
//    @Bean
//    public HashOperations<String, String, String> hashOperations(RedisTemplate<String, String> redisTemplate) {
//        return redisTemplate.opsForHash();
//    }
//
//    @Bean
//    public HashOperations<String, String, Object> hashOperationsWithValueJackson2JsonSerializer(RedisTemplate<String, Object> redisTemplate) {
//        return redisTemplate.opsForHash();
//    }
//    /**
//     * 对链表类型的数据操作
//     *
//     * @param redisTemplate
//     * @return
//     */
//    @Bean
//    public ListOperations<String, String> listOperations(RedisTemplate<String, String> redisTemplate) {
//        return redisTemplate.opsForList();
//    }
//
//    /**
//     * 对无序集合类型的数据操作
//     *
//     * @param redisTemplate
//     * @return
//     */
//    @Bean
//    public SetOperations<String, String> setOperations(RedisTemplate<String, String> redisTemplate) {
//        return redisTemplate.opsForSet();
//    }
//
//    /**
//     * 对有序集合类型的数据操作
//     *
//     * @param redisTemplate
//     * @return
//     */
//    @Bean
//    public ZSetOperations<String, String> zSetOperations(RedisTemplate<String, String> redisTemplate) {
//        return redisTemplate.opsForZSet();
//    }
    
}
