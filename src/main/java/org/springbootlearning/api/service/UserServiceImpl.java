package org.springbootlearning.api.service;

import java.util.Date;

import org.ootb.espresso.facilities.DateUtils;
import org.ootb.espresso.facilities.IDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbootlearning.api.dao.entity.UserBaseInfoDO;
import org.springbootlearning.api.dao.mapper.UserBaseInfoDOMapper;
import org.springbootlearning.api.exception.TemplateBizException;
import org.springbootlearning.api.service.dto.UserBaseInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//@CacheConfig(cacheManager = "redisCacheManager",cacheNames = "userCache")
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserBaseInfoDOMapper userBaseInfoDOMapper;
  
    
    @Override
    public boolean add(UserBaseInfoDTO userDTO) throws TemplateBizException {
        UserBaseInfoDO userBaseInfoDO = new UserBaseInfoDO();
        userBaseInfoDO.setCreateTime(new Date());
        userBaseInfoDO.setGender(userDTO.getGender());
        userBaseInfoDO.setLastModifyTime(new Date());
        userBaseInfoDO.setNickName(userDTO.getNickName());
        userBaseInfoDO.setUserId(IDGenerator.next());
        userBaseInfoDO.setUserName(userDTO.getUserName());
        userBaseInfoDO.setUserNameType(userDTO.getUserNameType());
        return userBaseInfoDOMapper.insert(userBaseInfoDO) == 1;
    }

    @Override
//    @Cacheable(key = "#id")
    public UserBaseInfoDTO queryById(long id) throws TemplateBizException {
        UserBaseInfoDO userBaseInfoDO = userBaseInfoDOMapper.selectByPrimaryKey(id);
        if(userBaseInfoDO == null){
            return null;
        }
        return toUserBaseInfoDTO(userBaseInfoDO);
    }

    private UserBaseInfoDTO toUserBaseInfoDTO(UserBaseInfoDO userBaseInfoDO) {
        UserBaseInfoDTO userBaseInfoDTO = new UserBaseInfoDTO();
        userBaseInfoDTO.setCreateTime(DateUtils.formatToStandardDateStringNow());
        userBaseInfoDTO.setGender(userBaseInfoDO.getGender());
        userBaseInfoDTO.setLastModifyTime(DateUtils.formatToStandardDateStringNow());
        userBaseInfoDTO.setNickName(userBaseInfoDO.getNickName());
        userBaseInfoDTO.setUserId(userBaseInfoDO.getUserId());
        userBaseInfoDTO.setUserName(userBaseInfoDO.getUserName());
        userBaseInfoDTO.setUserNameType(userBaseInfoDO.getUserNameType());
        return userBaseInfoDTO;
    }

    @Override
    public UserBaseInfoDTO queryByUserName(String userName) throws TemplateBizException {
        UserBaseInfoDO userBaseInfoDO = userBaseInfoDOMapper.selectByUserName(userName);
        if(userBaseInfoDO == null){
            return null;
        }
        return toUserBaseInfoDTO(userBaseInfoDO);
    }

}
