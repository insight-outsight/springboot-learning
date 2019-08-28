package org.springbootlearning.api.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ootb.espresso.facilities.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbootlearning.api.common.enumerate.ResultCodeEnum;
import org.springbootlearning.api.controller.dto.ErrorResponse;
import org.springbootlearning.api.controller.dto.IllegalRequestArgumentErrorResponse;
import org.springbootlearning.api.controller.dto.MissingRequestArgumentErrorResponse;
import org.springbootlearning.api.controller.dto.SystemErrorResponse;
import org.springbootlearning.api.exception.TemplateBizException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);
    
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ErrorResponse exp(Exception ex, HttpServletRequest request,
            HttpServletResponse response) {

        String url = null;
        try {
            url = request.getRequestURL().toString();
            if(request.getQueryString()!=null){
                url +=  "?"+request.getQueryString();
            }
        } catch (Exception e) {
            LOG.error("get url Error,", e);
        }
        if (ex instanceof HttpMessageNotReadableException) {
            LOG.error("spring mvc parse json error. request URL:{},ErrStackTrace:{}", url, ExceptionUtils.getStackTrace(ex,10));
            return new IllegalRequestArgumentErrorResponse();
        } else if (ex instanceof MissingServletRequestParameterException) {
            LOG.error("spring mvc params missing. request URL:{},ErrStackTrace:{}", url, ExceptionUtils.getStackTrace(ex,10));
            return new MissingRequestArgumentErrorResponse();
        } else if (ex instanceof TypeMismatchException) {
            LOG.error("spring mvc mime type not match. request URL:{},ErrStackTrace:{}", url, ExceptionUtils.getStackTrace(ex,10));
            LOG.error("{}", ExceptionUtils.getStackTrace(ex,10));
            return new IllegalRequestArgumentErrorResponse();
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            LOG.error("spring mvc meida type not acceptable. request URL:{},ErrStackTrace:{}", url, ExceptionUtils.getStackTrace(ex,10));
            return new IllegalRequestArgumentErrorResponse();
        } else if (ex instanceof IllegalArgumentException) {
            LOG.error("illegal argument. request URL:{},ErrStackTrace:{}", url, ExceptionUtils.getStackTrace(ex,10));
            return new IllegalRequestArgumentErrorResponse();
        } 
        else {
            LOG.error("URL:{},ErrStackTrace:{}", url, ExceptionUtils.getStackTrace(ex,10));
            return new SystemErrorResponse();
        }

    }

    @ExceptionHandler(TemplateBizException.class)
    @ResponseBody
    public ErrorResponse expBiz(Exception ex, HttpServletRequest request,
            HttpServletResponse response) {

        String url = null;
        try {
            url = request.getRequestURL().toString();
            if(request.getQueryString()!=null){
                url +=  "?"+request.getQueryString();
            }
            
            LOG.error("request URL:{},\n{}", url, ExceptionUtils.getStackTrace(ex,15));
        } catch (Exception e) {
            LOG.error("", ExceptionUtils.getStackTrace(e));
        }
        
        return new ErrorResponse(((TemplateBizException) ex).getErrorCode());

    }
    
    protected void checkRequestParamCompanyId(Integer companyId) throws TemplateBizException {
        if(companyId == null || companyId < 1) {
            throw new TemplateBizException(ResultCodeEnum.IllegalRequestArgument);
        }
    }

    protected void checkRequestParamUserId(Long userId,Long userIdParam) throws TemplateBizException {
        if(userId < 1){
            throw new TemplateBizException(ResultCodeEnum.UserNotExist);
        }
        if(userIdParam == null){
            throw new TemplateBizException(ResultCodeEnum.MissingRequestParameter);
        }
        if(userId.longValue() != userIdParam.longValue()){
            throw new TemplateBizException(ResultCodeEnum.IllegalRequestArgument);
        }
    }

    protected void checkRequestParamNonce(Long nonce)
            throws TemplateBizException {
        if(nonce == null || nonce < 1){
            throw new TemplateBizException(ResultCodeEnum.MissingRequestParameter);
        }
    }
    
}
