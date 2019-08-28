package org.springbootlearning.api.controller.dto;

/**
 *
 */
public class UserRegisterRequest extends CommonRequest{

    private String userName;
    private Integer userNameType;
    private String nickName;
    private Integer gender;
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Integer getUserNameType() {
        return userNameType;
    }
    public void setUserNameType(Integer userNameType) {
        this.userNameType = userNameType;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public Integer getGender() {
        return gender;
    }
    public void setGender(Integer gender) {
        this.gender = gender;
    }
    @Override
    public String toString() {
        return "UserRegisterRequest [userName=" + userName + ", userNameType=" + userNameType + ", nickName=" + nickName
                + ", gender=" + gender + "]";
    }

    

}
