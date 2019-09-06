package org.springbootlearning.api.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration
public class RedisLuaScriptConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedisLuaScriptConfig.class);

    @Bean
    public DefaultRedisScript<List> defaultRedisScript() {
        DefaultRedisScript<List> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setResultType(List.class);
       defaultRedisScript.setScriptText("--获取KEY\n" + 
               "local key1 = KEYS[1]\n" + 
               "local key2 = KEYS[2]\n" + 
               " \n" + 
               "--  注意，这里接收到是的字符串，所以需要用csjon库解码成table类型\n" + 
               "local receive_arg_json =  cjson.decode(ARGV[1])\n" + 
               " \n" + 
               "--获取ARGV内的参数并打印\n" + 
               "local expire = receive_arg_json.expire\n" + 
               "local times = receive_arg_json.times\n"
               + "return {expire,times}");
//        defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/demo.lua")));
        return defaultRedisScript;
    }
 
}
