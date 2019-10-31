package org.springbootlearning.api.controller.getBeanByProgramming;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbootlearning.api.config.ApplicationContextConfig;
import org.springbootlearning.api.getBeanByProgramming.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest({ "spring.profiles.active=dev" })
public class TestServiceFactoryBeanTest {

//    @Autowired
//    private TestServiceFactoryBean testServiceFactoryBean;

    @Autowired
    @Qualifier("tsi2")
    private TestService testService;

    @Autowired
    private ApplicationContextConfig applicationContextConfig;

    @Test
    public void test() throws Exception {
//        testServiceFactoryBean.settStr("t1");
//        TestService testService1 = testServiceFactoryBean.getObject();
//        testService1.test();
//        testServiceFactoryBean.settStr("t2");
//        TestService testService2 = testServiceFactoryBean.getObject();
//        testService2.test();
//        System.out.println(testService1 == testService2);
        testService.test();
//        System.out.println(testService1 == testService);
//
//        System.out.println(testService == testService2);
        TestService testServiceGB = applicationContextConfig.getContext().getBean("tsi1", TestService.class);
        testServiceGB.setTtStr("1111");
        testServiceGB.test();

        System.out.println(testService == testServiceGB);
        System.out.println(testService.getRedisService() == testServiceGB.getRedisService());
        TestService testServiceGB2 = applicationContextConfig.getContext().getBean("tsi1", TestService.class);
        testServiceGB2.setTtStr("222");
        testServiceGB2.test();
        System.out.println(testServiceGB == testServiceGB2);
        System.out.println(testServiceGB.getRedisService() == testServiceGB2.getRedisService());
        testServiceGB.test();
        testServiceGB2.test();
        System.out.println(testServiceGB.getTtStr());
        System.out.println(testServiceGB2.getTtStr());

//        testServiceFactoryBean.settStr("t26");
//        TestService testServiceGB3 = 
//                applicationContextConfig.getContext().getBean(TestService.class);
//        testServiceGB3.test();
//        
////        System.out.println(testService == testServiceGB);
//
//        testServiceFactoryBean.settStr("t44");
//        TestService testService4 = testServiceFactoryBean.getObject();
//        testService4.test();
//        TestService testServiceGB5 = 
//                applicationContextConfig.getContext().getBean(TestService.class);
//        testServiceGB5.test();
    }

}
