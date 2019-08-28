package org.springbootlearning.api.common.enumerate;

public enum ResultCodeEnum {


    Okay(0, "Okay"),
    
    SystemError(1101, "System Error！"),
    
    IllegalRequestArgument(3300, "Illegal Request Argument！"),
    MissingRequestParameter(3301, "Missing Request Parameter！"),
    UnknownAppType(3302, "Unknown AppType！"),
    
    UserNotExist(5513, "User Not Exist！"), 
    SessionTokenExpired(5514, "Session Token Expired！"),
    SessionTokenInvalid(5515, "Session Token Invalid！"),
    
    ;

    private final int code;
    private final String message;

    private ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
