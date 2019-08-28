package org.springbootlearning.api.controller.dto;

import org.springbootlearning.api.common.enumerate.ResultCodeEnum;

public class BaseOkayResponse<T> extends BaseResponse<T> {

    public BaseOkayResponse() {
        super(ResultCodeEnum.Okay.getCode(), ResultCodeEnum.Okay.getMessage());
    }

}
