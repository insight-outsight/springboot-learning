package org.springbootlearning.api.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@ConditionalOnClass({ JedisCluster.class })
public class RedisClusterConfig {
    
    @Value("${spring.redis.cluster.nodes}")
    private String clusterNodes;
    @Value("${spring.redis.cluster.max-redirects}")
    private int maxRedirects;
    @Value("${spring.redis.cluster.timeout}")
    private int timeout;
    @Value("${spring.redis.cluster.jedis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.cluster.jedis.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.cluster.jedis.pool.max-wait}")
    private long maxWaitMillis;
    @Value("${spring.redis.cluster.jedis.pool.max-active}")
    private int maxActive;
    @Value("${spring.redis.maxAttempts}")
    private int maxAttempts;
//    @Value("${spring.redis.password}")
//    private String password;

    @Bean
    public JedisCluster getJedisCluster() {
        String[] cNodes = clusterNodes.split(",");
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        
        for (String node : cNodes) {
            String[] hp = node.split(":");
            nodes.add(new HostAndPort(hp[0], Integer.parseInt(hp[1])));
        }
        
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setMaxTotal(maxActive);
        JedisCluster jedisCluster = new JedisCluster(nodes, timeout, timeout, maxAttempts, jedisPoolConfig);
        return jedisCluster;
    }
    
    @Bean(name = "redisClusterConnectionFactory")
    public RedisConnectionFactory connectionFactory() {
        String[] cNodes = clusterNodes.split(",");
        List<String> clusterNodesList = Arrays.asList(cNodes);
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterNodesList);
        redisClusterConfiguration.setMaxRedirects(maxRedirects);
      return new JedisConnectionFactory(redisClusterConfiguration);
    }
    
    @Bean("tRedisClusterTemplate")
    public RedisTemplate<String, String> redisTemplate(@Qualifier("redisClusterConnectionFactory") RedisConnectionFactory factory) {
      return new StringRedisTemplate(factory);
    }
    
}