package org.springbootlearning.api.controller.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbootlearning.api.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
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
    

}
