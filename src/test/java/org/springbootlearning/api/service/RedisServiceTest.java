/**
 * 
 */
package org.springbootlearning.api.service;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springbootlearning.api.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author admin
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest({"spring.profiles.active=dev"})
public class RedisServiceTest {

    @Autowired
    private RedisService redisService;
    
    @Test
    public void testStringSet() {
        redisService.set("rs_k5", "rs_k5_value");
    }
    
    @Test
    public void testStringGet() {
        System.out.println(redisService.get("rs_k5"));
    }
    
    @Test
    public void testHashSet() {
        redisService.hset("rs_hk5", "rs_hk5_f1", "rs_hk5_value");
    }
    
    @Test
    public void testHashGet() {
        System.out.println(redisService.hget("rs_hk5", "rs_hk5_f1"));
    }
    
    @Test
    public void testHincrbyWithExpired() {
        System.out.println(redisService.hincrbyWithExpired("rs_hi", "2290", 1, 988));
    }
    
    @Test
    public void testExpire() {
        redisService.expireKey("rs_hi", 60, TimeUnit.SECONDS);
    }
    
    @Test
    public void testHincrbyWithExpiredInTransactionV2(){
        System.out.println(redisService.hincrbyWithExpiredInTransactionV2("rs_hi5", "f700-0", 1, 300));
    }

}
