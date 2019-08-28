package org.springbootlearning.api.service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ootb.espresso.facilities.IDGenerator;
import org.springbootlearning.api.exception.TemplateBizException;
import org.springbootlearning.api.service.UserService;
import org.springbootlearning.api.service.dto.UserBaseInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends BaseServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testAdd() throws TemplateBizException {
        UserBaseInfoDTO record = new UserBaseInfoDTO();
        
        record.setGender(1);
        record.setNickName("test002-nn");
        record.setUserId(IDGenerator.next());
        record.setUserName("arch003@gmail.com");
        record.setUserNameType(1);
        
        System.out.println(userService.add(record));
    }

    @Test
    public void testQueryById() throws TemplateBizException {
        System.out.println(userService.queryById(5));
    }
    
    @Test
    public void testQueryByUserName() throws TemplateBizException {
        System.out.println(userService.queryByUserName("arch003@gmail.com"));
    }

}
