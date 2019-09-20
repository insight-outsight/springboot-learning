package org.springbootlearning.api.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbootlearning.api.constant.RedisMessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class RedisMessageListenerManager {
    
    private Logger logger = LoggerFactory.getLogger(RedisMessageListenerManager.class);
    
    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;
    
    @Autowired
    private MessageListenerAdapter listenerAdapter;
    
    @PostConstruct
    public void addMessageListener() {
        redisMessageListenerContainer.addMessageListener(listenerAdapter, new ChannelTopic(RedisMessageConstants.SUBSCRIBED_CHANNEL_TOPIC_TESTRP1));
        logger.info("added redis message listenerr");
    }
    
    @PreDestroy
    public void removeMessageListener() {
        redisMessageListenerContainer.removeMessageListener(listenerAdapter);
        logger.info("removed redis message listenerr");
    }
    
}