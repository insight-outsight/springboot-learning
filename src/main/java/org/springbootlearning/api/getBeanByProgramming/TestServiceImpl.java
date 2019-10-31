package org.springbootlearning.api.getBeanByProgramming;

import org.springbootlearning.api.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("tsi1")
@Scope("prototype")
public class TestServiceImpl implements TestService {

    private String ttStr;
    @Autowired
    private RedisService redisService;

    public TestServiceImpl() {
        super();
    }

    public TestServiceImpl(String ttStr) {
        this.ttStr = ttStr;
    }
    
    public TestServiceImpl(String ttStr, RedisService redisService) {
        this.ttStr = ttStr;
        this.redisService = redisService;
    }

    public RedisService getRedisService() {
        return redisService;
    }

    @Override
    public void test() {
//        System.out.println("t="+tStr);
        System.out.println("t="+ttStr+",redis get = "+redisService.get("rs_k5"));
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
