package org.springbootlearning.api.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springbootlearning.api.constant.RedisMessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest({"spring.profiles.active=dev"})
public class RedisMessagePubSubTest {

    @Autowired
    private RedisMessagePublisher redisMessagePublisher;
    
    @Test
    public void test() throws Exception {
        redisMessagePublisher.publish(RedisMessageConstants.SUBSCRIBED_CHANNEL_TOPIC_TESTRP1, "imntf4");
        System.out.println("done");
        Thread.sleep(60000);
    }
    @Test
    public void test2() throws Exception {
        System.out.println("done2");
        Thread.sleep(60000);
    }

    
}
