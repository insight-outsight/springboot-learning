package org.springbootlearning.api.exception;

import org.ootb.espresso.facilities.BaseBizException;
import org.springbootlearning.api.common.enumerate.ResultCodeEnum;

public class TemplateBizException extends BaseBizException {

    /**
     * 
     */
    private static final long serialVersionUID = 134593975815518737L;

    // 返回给客户的响应状态码
    private final ResultCodeEnum errorCode;

    // 内部标识错误信息的响应码
    private String innerErrorCode;

    public TemplateBizException(ResultCodeEnum errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

    public TemplateBizException(ResultCodeEnum errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public TemplateBizException(ResultCodeEnum errorCode, String message,
            Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public TemplateBizException(ResultCodeEnum errorCode, String innerErrorCode,
            String message) {
        super(message);
        this.errorCode = errorCode;
        this.innerErrorCode = innerErrorCode;
    }

    public TemplateBizException(ResultCodeEnum errorCode, String innerErrorCode,
            String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.innerErrorCode = innerErrorCode;
    }

    public String getInnerErrorCode() {
        return innerErrorCode;
    }

    public void setInnerErrorCode(String innerErrorCode) {
        this.innerErrorCode = innerErrorCode;
    }

    public ResultCodeEnum getErrorCode() {
        return errorCode;
    }

}
