package org.springbootlearning.api.getBeanByProgramming;

import org.springbootlearning.api.service.RedisService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;


//@Component
public class TestServiceFactoryBean implements FactoryBean<TestService> {

    private String tStr;

    @Autowired
    private RedisService redisService;
    
    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public TestService getObject() throws Exception {
        return new TestServiceImpl(tStr,redisService);
    }

    @Override
    public Class<?> getObjectType() {
        return TestService.class;
    }

    public String gettStr() {
        return tStr;
    }

    public void settStr(String tStr) {
        this.tStr = tStr;
    }
    
}