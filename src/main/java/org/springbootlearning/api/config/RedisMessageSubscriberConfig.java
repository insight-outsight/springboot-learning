package org.springbootlearning.api.config;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbootlearning.api.constant.RedisMessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

@Configuration
public class RedisMessageSubscriberConfig {

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
//        container.addMessageListener(listenerAdapter, new ChannelTopic(RedisMessageConstants.SUBSCRIBED_CHANNEL_TOPIC_TESTRP1));
        //这个container 可以添加多个 messageListener
//        container.addMessageListener(listenerAdapter, new PatternTopic("testkafka1"));// 配置要订阅的订阅项
        return container;
    }

    /**
     * 使用MessageListenerAdapter方式
     * @author 
     *
     */
    @Component
    public class RedisSubscriber extends MessageListenerAdapter {

        private Logger logger = LoggerFactory.getLogger(RedisSubscriber.class);
        
        @Autowired
        private RedisTemplate<String, String> redisTemplate;

        @Override
        public void onMessage(Message message, byte[] pattern) {
            try {
                String channel = new String(message.getChannel());
                String body = new String(message.getBody());
                logger.debug("receive redis published message channel:{},body:{}",channel,body);
                if(RedisMessageConstants.SUBSCRIBED_CHANNEL_TOPIC_TESTRP1.equals(channel)) {
                    String topic = redisTemplate.getStringSerializer().deserialize(message.getChannel());
                    String msg = redisTemplate.getStringSerializer().deserialize(message.getBody());
                    System.out.println("监听到topic为[" + topic + "]的消息：" + msg);
                }
            } catch (Exception e) {
                logger.error("",e);
            }
        }

    }
    
    /**
     * 也可以消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     * @param receiver
     * @return
     */
//    @Bean
//    MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
//        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
//        //也有好几个重载方法，这边默认调用处理器的方法 叫handleMessage 可以自己到源码里面看
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }
//    
//    @Component
//    public class MessageReceiver {
//
//        /**接收消息的方法*/
//        public void receiveMessage(String message){
//            System.out.println("收到一条消息："+message);
//        }
//    }
    
    /**
     * 还可以直接使用MessageListener方式
     */
//    @Component("testSubscriber")
//    public class Subscriber implements MessageListener{
//        
//        @Autowired
//        private StringRedisSerializer stringRedisSerializer;
//        @Autowired
//        private GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer;
//        
//        @Override
//        public void onMessage(Message message, byte[] pattern) {
//            byte[] body = message.getBody();//请使用valueSerializer  
//            byte[] channel = message.getChannel();  
//            String msg = (String)stringRedisSerializer.deserialize(body);  
//            String topic = (String)stringRedisSerializer.deserialize(channel);
//            System.out.println("我是sub,监听"+topic+",我收到消息："+msg);
//        }
//
//    }
    
}
