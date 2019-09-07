/**
 * 
 */
package org.springbootlearning.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ootb.espresso.facilities.JacksonJSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;

import redis.clients.jedis.JedisCluster;

/**
 * @author admin
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest({"spring.profiles.active=dev"})
public class StringRedisTemplateTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Autowired
    private RedisTemplate<String, Bread> tRedisTemplate;
    
    @Qualifier("tRedisTemplate")
    @Autowired
    private RedisTemplate<String, Map> mapRedisTemplate;
    
    @Autowired
    private JedisCluster jedisCluster;
    
    @Qualifier("tRedisClusterTemplate")
    @Autowired
    private RedisTemplate<String, String> redisClusterTemplate;
    
    @Autowired
    private ValueOperations<String,String> valueOperations;

    @Autowired
    private HashOperations<String, String, String> hashOperations;

    @Autowired
    private ListOperations<String, String> listOperations;

    @Autowired
    private SetOperations<String, String> setOperations;

    @Autowired
    private ZSetOperations<String, String> zSetOperations;


    @Test
    public void testEq() {
        System.out.println(stringRedisTemplate);
        System.out.println(redisTemplate);
        System.out.println(tRedisTemplate);
        System.out.println("1="+(redisTemplate == stringRedisTemplate));
    }
    
    @Test
    public void testStringSet() {
        stringRedisTemplate.opsForValue().set("srt_k2", "srt_value234");
    }
    
    @Test
    public void testStringGet() {
        System.out.println(stringRedisTemplate.opsForValue().get("srt_k2"));
    }
    
    @Test
    public void testObjectSet() {
        Bread bread = new Bread();
        bread.setMadeIn("Italy");
        bread.setName("BlackBread");
        tRedisTemplate.opsForValue().set("o_k2", bread);
    }
    
    @Test
    public void testObjectGet() {
        Bread bread = tRedisTemplate.opsForValue().get("o_k2");
        System.out.println(bread.getName());
        System.out.println(bread.getMadeIn());
    }
    
    @Test
    public void testHashSet() {
        hashOperations.put("ho_k1", "ho_k1_f1", "ho_k1_value1");
    }
    
    @Test
    public void testHashGet() {
        System.out.println(hashOperations.get("ho_k1", "ho_k1_f1"));
    }
    
    @Test
    public void testListLpush() {
        System.out.println(listOperations.leftPush("l_k11", "er"));
    }
    
    @Test
    public void testListLpushAll() {
        ArrayList<String> elements = Lists.newArrayList();
        System.out.println(elements);
        System.out.println(listOperations.leftPushAll("l_k11b", elements));
    }
    
    @Test
    public void testListRpop() {
        System.out.println(listOperations.rightPop("l_k11b"));
    }
    
    @Test
    public void testListRange() {
        System.out.println(listOperations.range("l_k11",0,-1));
    }
    
    @Autowired
    DefaultRedisScript<List> defaultRedisScript;
    
    @Test
    public void testLuaScript() {
        System.out.println(tRedisTemplate);
        System.out.println(mapRedisTemplate);
        /**
        /**
         * List设置lua的KEYS
         */
        List<String> keyList = new ArrayList<String>();
        keyList.add("count");
        keyList.add("rate.limiting:127.0.0.1");
        
        /**
         * 用Map设置Lua的ARGV[1]
         */
        Map<String, Object> argvMap = new HashMap<String, Object>();
        argvMap.put("expire", 10000);
        argvMap.put("times", 10);
        
        defaultRedisScript.setScriptText(
                "local key1 = KEYS[1];"
                + "local key2 = KEYS[2];"
                + "local receive_arg_json =  cjson.decode(ARGV[1]);"
                + "local expire = receive_arg_json.expire;"
                + "local times = receive_arg_json.times;"
                + "return {expire,'omg',times}");
        
        List<?> result = stringRedisTemplate.execute(defaultRedisScript, keyList, JacksonJSONUtils.toJSON(argvMap));

        System.out.println(result);
        
        //可以获取脚本SHA1
        System.out.println(defaultRedisScript.getSha1());
//        若是出现序列化问题，可以指定序列化方式。
//        public <T> T execute(RedisScript<T> script, RedisSerializer<?> argsSerializer, RedisSerializer<T> resultSerializer,
//                List<K> keys, Object... args) {
//            return scriptExecutor.execute(script, argsSerializer, resultSerializer, keys, args);
//        }
    }
    
    
    @Test
    public void testLuaScript1() {
       
        List<String> keyList = new ArrayList<String>();
        keyList.add("user:1");
        keyList.add("user:2");
        
        defaultRedisScript.setScriptText(
                "local rst={}; for i,v in pairs(KEYS) do rst[i]=redis.call('hgetall', v) end; return rst");

        List<?> result = stringRedisTemplate.execute(defaultRedisScript, keyList);
        
        System.out.println(result);
        System.out.println(defaultRedisScript.getSha1());

        
    }
    
    @Test
    public void testLuaScript2() {
        
        List<String> keyList = new ArrayList<String>();
        keyList.add("key:11");
        keyList.add("key:22");
        keyList.add("key:33");
        
        defaultRedisScript.setScriptText(
                "local ret0={};ret0[1]=redis.call('lpush',KEYS[1],ARGV[1]);ret0[2]=redis.call('set',KEYS[2],ARGV[2]);ret0[3]=redis.call('set',KEYS[3],ARGV[3]);return ret0");
        
        List<?> result = stringRedisTemplate.execute(defaultRedisScript, keyList, "v11123", "v22234", "v33345");
        
        System.out.println(result);
//        System.out.println(defaultRedisScript.getSha1());
        
        
    }
    
    @Test
    public void testRedisClusterSet() {
        redisClusterTemplate.opsForValue().set("srt_k2", "srt_value23457");
    }
    
    @Test
    public void testJedisClusterSet() {
        jedisCluster.set("srt_k22", "srt_value22");
    }

}
