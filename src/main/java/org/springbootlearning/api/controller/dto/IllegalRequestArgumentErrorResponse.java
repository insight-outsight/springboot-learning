package org.springbootlearning.api.controller.dto;

import org.springbootlearning.api.common.enumerate.ResultCodeEnum;

public class IllegalRequestArgumentErrorResponse extends ErrorResponse {

    public IllegalRequestArgumentErrorResponse() {
        super(ResultCodeEnum.IllegalRequestArgument);
    }

}
