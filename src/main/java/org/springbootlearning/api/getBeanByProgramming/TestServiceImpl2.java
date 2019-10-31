package org.springbootlearning.api.getBeanByProgramming;

import org.springbootlearning.api.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("tsi2")
public class TestServiceImpl2 implements TestService {

    private String ttStr;
    @Autowired
    private RedisService redisService;

    public TestServiceImpl2() {
        super();
    }

    public TestServiceImpl2(String ttStr) {
        this.ttStr = ttStr;
    }
    
    public TestServiceImpl2(String ttStr, RedisService redisService) {
        this.ttStr = ttStr;
        this.redisService = redisService;
    }

    public RedisService getRedisService() {
        return redisService;
    }

    @Override
    public void test() {
        System.out.println("t="+ttStr+",redis get = "+redisService.get("rs_k5"));
//        System.out.println("t="+tStr);
    }
    
    @Override
    public String getTtStr() {
        return ttStr;
    }

    @Override
    public void setTtStr(String ttStr) {
        this.ttStr = ttStr;
    }
    
    

}
