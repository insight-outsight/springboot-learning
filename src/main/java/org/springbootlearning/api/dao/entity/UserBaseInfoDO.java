package org.springbootlearning.api.dao.entity;

import java.util.Date;

public class UserBaseInfoDO {
    private Long id;

    private Long userId;

    private Integer userNameType;

    private String userName;

    private String nickName;

    private Integer gender;

    private Date createTime;

    private Date lastModifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    @Override
    public String toString() {
        return "UserBaseInfoDO [id=" + id + ", userId=" + userId + ", userNameType=" + userNameType + ", userName="
                + userName + ", nickName=" + nickName + ", gender=" + gender + ", createTime=" + createTime
                + ", lastModifyTime=" + lastModifyTime + "]";
    }
    
    
}