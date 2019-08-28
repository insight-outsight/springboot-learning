package org.springbootlearning.api.controller;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springbootlearning.api.controller.dto.UserRegisterRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.google.common.collect.Lists;

public class UserControllerTest extends BaseControllerTest {

    @Test
    public void testLogin() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/login.do")
                .param("userName", "arch003@gmail.com")
                .header("appType", "1")
                .header("appVersion", "101")
                .contentType(MediaType.APPLICATION_JSON)
                .content("")).andReturn();// 返回执行请求的结果  

        int status = mvcResult.getResponse().getStatus();  
        String content = mvcResult.getResponse().getContentAsString();
          
        
        System.out.println("HTTP Response Status Code:"+status);
        System.out.println("HTTP Response Body Data:"+content);
        
    }

    @Test
    public void testRegister() throws Exception {
        
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/register.do")
                .param("gender", "1")
                .param("nickName", "user-test_b_nn")
                .param("userName", "user-test_b")
                .param("userNameType", "20")
                .header("appType", "1")
                .header("appVersion", "101")
                .contentType(MediaType.APPLICATION_JSON)
                .content("")).andReturn();// 返回执行请求的结果  

        int status = mvcResult.getResponse().getStatus();  
        String content = mvcResult.getResponse().getContentAsString();
          
        
        System.out.println("HTTP Response Status Code:"+status);
        System.out.println("HTTP Response Body Data:"+content);
        
    }


}
