package org.springbootlearning.api.controller.dto;

import org.springbootlearning.api.common.enumerate.ResultCodeEnum;

public class SystemErrorResponse extends ErrorResponse {

    public SystemErrorResponse() {
        super(ResultCodeEnum.SystemError);
    }

}
