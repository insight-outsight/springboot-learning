package org.springbootlearning.api.service.dto;

public class UserFeatureModelDTO {

    private Long userId;
    private Integer appType;
    private Integer appVersion;
    private Integer provinceCode;
    private Integer cityCode;
    private Integer countyCode;
    private Integer sex;
    
    public UserFeatureModelDTO() {
        super();
    }

    public UserFeatureModelDTO(Integer appType, Integer appVersion) {
        super();
        this.appType = appType;
        this.appVersion = appVersion;
    }
    
    public UserFeatureModelDTO(Long userId, Integer appType, Integer appVersion) {
        super();
        this.userId = userId;
        this.appType = appType;
        this.appVersion = appVersion;
    }
    
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Integer getAppType() {
        return appType;
    }
    public void setAppType(Integer appType) {
        this.appType = appType;
    }
    public Integer getAppVersion() {
        return appVersion;
    }
    public void setAppVersion(Integer appVersion) {
        this.appVersion = appVersion;
    }
    public Integer getProvinceCode() {
        return provinceCode;
    }
    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }
    public Integer getCityCode() {
        return cityCode;
    }
    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }
    public Integer getCountyCode() {
        return countyCode;
    }
    public void setCountyCode(Integer countyCode) {
        this.countyCode = countyCode;
    }
    public Integer getSex() {
        return sex;
    }
    public void setSex(Integer sex) {
        this.sex = sex;
    }
    @Override
    public String toString() {
        return "UserFeatureModelDTO [userId=" + userId + ", appType=" + appType
                + ", appVersion=" + appVersion + ", provinceCode="
                + provinceCode + ", cityCode=" + cityCode + ", countyCode="
                + countyCode + ", sex=" + sex + "]";
    }

}
