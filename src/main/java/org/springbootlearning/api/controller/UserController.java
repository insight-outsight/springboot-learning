package org.springbootlearning.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbootlearning.api.controller.dto.LoginResponse;
import org.springbootlearning.api.controller.dto.LoginResponseResult;
import org.springbootlearning.api.controller.dto.SimpleOkayResponse;
import org.springbootlearning.api.controller.dto.UserRegisterRequest;
import org.springbootlearning.api.service.UserService;
import org.springbootlearning.api.service.dto.UserBaseInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
            
    @Autowired
    private UserService userService;

    /**
     * 注册
     * @throws Exception 
     */
    @PostMapping(value = "/register")
    public SimpleOkayResponse register(HttpServletRequest request, HttpServletResponse response,
            UserRegisterRequest userRegisterRequest,
            @RequestHeader(value = "appType", required = true) Integer appType,
            @RequestHeader(value = "appVersion", required = true) Integer appVersion) throws Exception {
        
        logger.debug("userRegisterRequest:{},appType:{},appVersion:{}",userRegisterRequest,appType,appVersion);
        UserBaseInfoDTO userDTO = new UserBaseInfoDTO();
        userDTO.setGender(1);
        userDTO.setNickName(userRegisterRequest.getNickName());
        userDTO.setUserName(userRegisterRequest.getUserName());
        userDTO.setUserNameType(userRegisterRequest.getUserNameType());
        boolean result = userService.add(userDTO);
        logger.debug("register status:{}",result);
        return new SimpleOkayResponse();
        
    }
    
    /**
     * 登录
     * @throws Exception 
     */
    @PostMapping(value = "/login")
    public LoginResponse login(HttpServletRequest request, HttpServletResponse response,
//            @RequestParam(value = "reqData", required = true) String reqDataJSON,
            @RequestParam(value = "userName", required = true) String userName,
            @RequestHeader(value = "appType", required = true) Integer appType,
            @RequestHeader(value = "appVersion", required = true) Integer appVersion) throws Exception {
        
        logger.debug("userName:{},appType:{},appVersion:{}",userName,appType,appVersion);
        UserBaseInfoDTO userBaseInfoDTO = userService.queryByUserName(userName);
        LoginResponse loginResponse = new LoginResponse();
        LoginResponseResult loginResponseResult = new LoginResponseResult();
        if( userBaseInfoDTO != null) {
            loginResponseResult.setNickName(userBaseInfoDTO.getNickName());
            loginResponseResult.setUserId(userBaseInfoDTO.getUserId());
            loginResponseResult.setUserName(userBaseInfoDTO.getUserName());
        }
        loginResponse.setResData(loginResponseResult);
        logger.debug("resData:{}",loginResponse);
        return loginResponse;
        
    }

}
