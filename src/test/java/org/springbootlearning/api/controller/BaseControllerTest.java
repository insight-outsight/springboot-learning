package org.springbootlearning.api.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;



@RunWith(SpringRunner.class)
@SpringBootTest({"spring.profiles.active=dev"})
@AutoConfigureMockMvc
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc; 
      
    @Autowired  
    protected WebApplicationContext wac; // 注入WebApplicationContext  
  
//  @Autowired  
//  protected MockHttpSession session;// 注入模拟的http session  
//    
//  @Autowired  
//  protected MockHttpServletRequest request;// 注入模拟的http request\  


    
//    @Before // 在测试开始前初始化工作  
//    public void setup() {  
        //MockMvcBuilders使用构建MockMvc对象（项目拦截器有效）
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();  
//    } 
    
    
 
    
}
