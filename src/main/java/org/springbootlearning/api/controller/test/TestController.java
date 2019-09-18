package org.springbootlearning.api.controller.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbootlearning.api.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class TestController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(TestController.class);
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping(value = "/isOk", method = RequestMethod.GET)
    public String imokay(HttpServletRequest request,HttpServletResponse response) {
        return "imok";
    }
    
    @GetMapping(value = "/slbp", produces = "application/json")
    public String simuLongBizProcess(HttpServletRequest request,HttpServletResponse response) {
        logger.info("模拟业务处理1分钟，请求参数：{}", request.getParameter("p2"));
        Long serverTime = System.currentTimeMillis();
    
        while (System.currentTimeMillis() < serverTime + (28 * 1000)) {
            if(System.currentTimeMillis() % 500 == 0) {
                logger.info("正在处理业务，当前时间：{}，开始时间：{}", System.currentTimeMillis(), serverTime);
            }
        }
        String resultEntity = "ok at "+System.currentTimeMillis();
        logger.info("模拟业务处理1分钟，请求参数：{}，响应参数：{}", request.getParameter("p2"), resultEntity);
        return resultEntity;
    }

}
