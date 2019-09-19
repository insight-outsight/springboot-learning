package org.springbootlearning.api.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest({"spring.profiles.active=dev"})
public class RedisMessageListenerTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Test
    public void test() throws Exception {
        redisTemplate.convertAndSend("testrp1", "imntf");
        System.out.println("done");
        Thread.sleep(100000);
    }
    @Test
    public void test2() throws Exception {
        System.out.println("done2");
        Thread.sleep(100000);
    }

    
}
