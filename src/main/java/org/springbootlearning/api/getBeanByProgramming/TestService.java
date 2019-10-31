package org.springbootlearning.api.getBeanByProgramming;

import org.springbootlearning.api.service.RedisService;

public interface TestService {

    void test();
    RedisService getRedisService();
    void setTtStr(String ttStr);
    String getTtStr();
}