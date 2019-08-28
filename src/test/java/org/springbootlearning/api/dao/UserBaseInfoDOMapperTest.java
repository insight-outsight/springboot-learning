/**
 * 
 */
package org.springbootlearning.api.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.ootb.espresso.facilities.IDGenerator;
import org.springbootlearning.api.dao.entity.UserBaseInfoDO;
import org.springbootlearning.api.dao.mapper.UserBaseInfoDOMapper;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author admin
 *
 */
public class UserBaseInfoDOMapperTest extends BaseDaoTest{

    
    @Autowired
    private UserBaseInfoDOMapper userBaseInfoDOMapper;

    @Test
    public void testSelectByPrimaryKey() {
        UserBaseInfoDO userBaseInfoDO = userBaseInfoDOMapper.selectByPrimaryKey(8L);
        System.out.println(userBaseInfoDO);
    }
    
    @Test
    public void testSelectByUserName() {
        UserBaseInfoDO userBaseInfoDO = userBaseInfoDOMapper.selectByUserName("arch003@gmail.com");
        System.out.println(userBaseInfoDO);
    }


    @Test
    public void testInsert() {
        UserBaseInfoDO record = new UserBaseInfoDO();
        record.setCreateTime(new Date());
        record.setGender(1);
        record.setLastModifyTime(new Date());
        record.setNickName("test146-ny");

        record.setUserId(IDGenerator.next());
        record.setUserName("arch146@gmail.com");
        record.setUserNameType(1);
        
        System.out.println(record);
        int effectedRow = userBaseInfoDOMapper.insert(record);
        System.out.println(effectedRow);
        assertEquals(1,effectedRow);
    }

}
