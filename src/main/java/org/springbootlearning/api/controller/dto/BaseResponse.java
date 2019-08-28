package org.springbootlearning.api.controller.dto;

public abstract class BaseResponse<T> {

    private Integer errorCode;
    private String errorMessage;

    private T resData;

    public BaseResponse() {
        super();
    }

    public BaseResponse(int errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BaseResponse(int errorCode, String errorMessage, T resData) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.resData = resData;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getResData() {
        return resData;
    }

    public void setResData(T resData) {
        this.resData = resData;
    }

    @Override
    public String toString() {
        return "BaseResponse [errorCode=" + errorCode + ", errorMessage="
                + errorMessage + ", resData=" + resData + "]";
    }

}
