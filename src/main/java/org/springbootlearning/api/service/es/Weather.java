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
    "biz_id",
    "biz_type",
    "o_id",
    "m_time"
})
public class Weather extends ESEntity{

    @JsonProperty("s_id")
    private Integer sId;
    @JsonProperty("biz_id")
    private Integer bizId;
    @JsonProperty("biz_type")
    private Integer bizType;
    @JsonProperty("o_id")
    private String oId;
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
    public Integer getSId() {
        return sId;
    }

    /**
     * 
     * @param sId
     *     The s_id
     */
    @JsonProperty("s_id")
    public void setSId(Integer sId) {
        this.sId = sId;
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
        return new HashCodeBuilder().append(sId).append(bizId).append(bizType).append(oId).append(mTime).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Weather) == false) {
            return false;
        }
        Weather rhs = ((Weather) other);
        return new EqualsBuilder().append(id, rhs.id).append(routingKey, rhs.routingKey).append(sId, rhs.sId).append(bizId, rhs.bizId).append(bizType, rhs.bizType).append(oId, rhs.oId).append(mTime, rhs.mTime).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}