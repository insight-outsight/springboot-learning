/**
 * 
 */
package org.springbootlearning.api.service;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import com.google.common.collect.Lists;

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
        stringRedisTemplate.opsForValue().set("srt_k2", "srt_value2");
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
    

}
