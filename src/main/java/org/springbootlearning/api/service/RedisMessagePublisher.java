package org.springbootlearning.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisMessagePublisher {

    private Logger logger = LoggerFactory.getLogger(RedisMessagePublisher.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void publish(String channelName, String message) {

        try {
            logger.debug("redis publish message channel:{},message:{}", channelName, message);
            redisTemplate.convertAndSend(channelName, message);
            logger.debug("redis published message");
        } catch (Exception e) {
            logger.error("", e);
        }
    }

}