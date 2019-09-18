package org.springbootlearning.api.service.es;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "s_id",
    "s_type",
    "biz_id",
    "biz_type",
    "o_id",
    "status",
    "m_time"
})
public class Counter extends ESEntity{

    @JsonProperty("s_id")
    private Long sId;
    @JsonProperty("s_type")
    private Integer sType;
    @JsonProperty("biz_id")
    private Integer bizId;
    @JsonProperty("biz_type")
    private Integer bizType;
    @JsonProperty("o_id")
    private String oId;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("m_time")
    private Long mTime;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The sId
     */
    @JsonProperty("s_id")
    public Long getSId() {
        return sId;
    }

    /**
     * 
     * @param sId
     *     The s_id
     */
    @JsonProperty("s_id")
    public void setSId(Long sId) {
        this.sId = sId;
    }

    /**
     * 
     * @return
     *     The sType
     */
    @JsonProperty("s_type")
    public Integer getSType() {
        return sType;
    }

    /**
     * 
     * @param sType
     *     The s_type
     */
    @JsonProperty("s_type")
    public void setSType(Integer sType) {
        this.sType = sType;
    }
    
    /**
     * 
     * @return
     *     The bizId
     */
    @JsonProperty("biz_id")
    public Integer getBizId() {
        return bizId;
    }

    /**
     * 
     * @param bizId
     *     The biz_id
     */
    @JsonProperty("biz_id")
    public void setBizId(Integer bizId) {
        this.bizId = bizId;
    }

    /**
     * 
     * @return
     *     The bizType
     */
    @JsonProperty("biz_type")
    public Integer getBizType() {
        return bizType;
    }

    /**
     * 
     * @param bizType
     *     The biz_type
     */
    @JsonProperty("biz_type")
    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    /**
     * 
     * @return
     *     The oId
     */
    @JsonProperty("o_id")
    public String getOId() {
        return oId;
    }

    /**
     * 
     * @param oId
     *     The o_id
     */
    @JsonProperty("o_id")
    public void setOId(String oId) {
        this.oId = oId;
    }

    /**
     * 
     * @return
     *     The status
     */
    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    @JsonProperty("s_type")
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    /**
     * 
     * @return
     *     The mTime
     */
    @JsonProperty("m_time")
    public Long getMTime() {
        return mTime;
    }

    /**
     * 
     * @param mTime
     *     The m_time
     */
    @JsonProperty("m_time")
    public void setMTime(Long mTime) {
        this.mTime = mTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(routingKey).append(sId).append(sType).append(bizId).append(bizType).append(oId).append(status).append(mTime).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Counter) == false) {
            return false;
        }
        Counter rhs = ((Counter) other);
        return new EqualsBuilder().append(id, rhs.id).append(routingKey, rhs.routingKey).append(sId, rhs.sId).append(sType, rhs.sType).append(bizId, rhs.bizId).append(bizType, rhs.bizType).append(oId, rhs.oId).append(status, rhs.status).append(mTime, rhs.mTime).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}