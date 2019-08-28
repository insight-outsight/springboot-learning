package org.springbootlearning.api.service.dto;

import java.util.Date;

public class UserBaseInfoDTO extends BaseToStringBuilderDTO {

    private Long userId;

    private Integer userNameType;

    private String userName;

    private String nickName;

    private Integer gender;

    private String createTime;

    private String lastModifyTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getUserNameType() {
        return userNameType;
    }

    public void setUserNameType(Integer userNameType) {
        this.userNameType = userNameType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    
}
