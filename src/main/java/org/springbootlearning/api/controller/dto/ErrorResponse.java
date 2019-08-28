package org.springbootlearning.api.controller.dto;

import org.springbootlearning.api.common.enumerate.ResultCodeEnum;

public class ErrorResponse extends BaseResponse<String> {

    public ErrorResponse(ResultCodeEnum resultCodeEnum) {
        this(resultCodeEnum.getCode(), resultCodeEnum.getMessage());
    }

    public ErrorResponse(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
        setResData(null);
    }

}
