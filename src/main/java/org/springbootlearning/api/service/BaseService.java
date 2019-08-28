package org.springbootlearning.api.service;


import org.ootb.espresso.facilities.DateUtils;
import org.ootb.espresso.facilities.InputFieldValidatorUtils;
import org.springbootlearning.api.common.enumerate.AppTypeEnum;
import org.springbootlearning.api.common.enumerate.ResultCodeEnum;
import org.springbootlearning.api.exception.TemplateBizException;



public abstract class BaseService {
    
    protected void checkUserExist(Long userId) throws TemplateBizException{
        if(userId == null){
            throw new TemplateBizException(ResultCodeEnum.IllegalRequestArgument);
        }
        
        if(userId < 1){
            throw new TemplateBizException(ResultCodeEnum.UserNotExist);
        }
        //TODO Check User Info
//        if(user not exist){
//            throw new JifenBizException(ResultCodeEnum.UserNotExist);
//
//        }
    }
    
    protected String geneActualTransactionSerialNum(AppTypeEnum appType, String tsn) throws TemplateBizException {
        checkAppType(appType);
        InputFieldValidatorUtils.validateTsn(tsn);
        StringBuilder sb = new StringBuilder(DateUtils.formatToCompactStyleDateString(System.currentTimeMillis()));
        sb.append(appType.getValue()).append(tsn);
        return sb.toString();
    }
/*    
    protected String geneRandomTransactionSerialNum(int appType) throws Exception {
        checkAppType(appType);
        StringBuilder sb = new StringBuilder(DateUtils.formatToCompactStyleDateString(System.currentTimeMillis()));
        sb.append(appType).append(RandomStringUtils.randomAlphanumeric(32));
        return sb.toString();
    }*/
    
    protected void checkAppType(int appType) throws TemplateBizException {
        AppTypeEnum appTypeEnum = AppTypeEnum.valueOf(appType);
        checkAppType(appTypeEnum);
    }
    
    protected void checkAppType(AppTypeEnum appType) throws TemplateBizException {
        if(appType == null){
            throw new TemplateBizException(ResultCodeEnum.UnknownAppType);
        }
    }
    
}
