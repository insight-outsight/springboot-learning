package org.springbootlearning.api.controller.dto;

import org.springbootlearning.api.common.enumerate.ResultCodeEnum;

public class MissingRequestArgumentErrorResponse extends ErrorResponse {

    public MissingRequestArgumentErrorResponse() {
        super(ResultCodeEnum.MissingRequestParameter);
    }

}
