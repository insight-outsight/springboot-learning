package org.springbootlearning.api.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springbootlearning.api.service.es.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest({"spring.profiles.active=dev"})
public class CacheServiceTest {

    @Autowired
    private CacheService<String> cacheService;
    @Autowired
    private CacheService<String> cacheService2;
    @Autowired
    private CacheService<Counter> cacheCService;
    
    @Test
    public void test() throws Exception {
        for(int i=0; i < 5; i++) {
            cacheService.set("k"+i,"v"+i);
            System.out.println("set "+i);
        }
        Thread.sleep(3*1000);
        for(int i=0; i < 10; i++) {
            System.out.println("get k"+i+"="+cacheService.get("k"+i));
        }
        Thread.sleep(30*1000);
    }
    
    @Test
    public void testCounter() throws Exception {
        System.out.println(cacheService);
        System.out.println(cacheCService);
        System.out.println(cacheService2);
        System.out.println(cacheService == cacheService2);
        long st = System.currentTimeMillis();
        for(int i=0; i < 10; i++) {
            Counter c  = new Counter();
            c.setId("idis"+i);
            c.setBizId(i);
            c.setBizType(i);
            c.setRoutingKey("routingKey"+i);
            c.setSId(System.currentTimeMillis()+i*10000);
            c.setOId(RandomStringUtils.randomAlphabetic(10));
            c.setMTime(System.currentTimeMillis());
            cacheCService.set("kc-"+i,c);
//            System.out.println("set kc-"+i);
        }
        System.out.println("set cost:"+(System.currentTimeMillis()-st));

        Thread.sleep(10*1000);
//        cacheCService.invalidate("kc-"+5);
//        cacheCService.invalidate("kc-"+7);
        st = System.currentTimeMillis();
        for(int i=0; i < 20; i++) {
            System.out.println("get k"+i+"="+cacheCService.get("kc-"+i));
        }
        System.out.println("get cost:"+(System.currentTimeMillis()-st));

        Thread.sleep(30*1000);
    }
    
}
