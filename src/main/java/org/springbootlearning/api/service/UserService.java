package org.springbootlearning.api.service;


import java.util.List;

import org.springbootlearning.api.exception.TemplateBizException;
import org.springbootlearning.api.service.dto.UserBaseInfoDTO;

public interface UserService {

    boolean add(UserBaseInfoDTO userDTO) throws TemplateBizException;
    UserBaseInfoDTO queryById(long id) throws TemplateBizException;
    UserBaseInfoDTO queryByUserName(String userName) throws TemplateBizException;

}
