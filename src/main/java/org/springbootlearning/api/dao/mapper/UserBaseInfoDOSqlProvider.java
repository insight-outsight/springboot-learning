package org.springbootlearning.api.dao.mapper;

import org.apache.ibatis.jdbc.SQL;
import org.springbootlearning.api.dao.entity.UserBaseInfoDO;

public class UserBaseInfoDOSqlProvider {

    public String insertSelective(UserBaseInfoDO record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("user_base_info");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getUserId() != null) {
            sql.VALUES("user_id", "#{userId,jdbcType=BIGINT}");
        }
        
        if (record.getUserNameType() != null) {
            sql.VALUES("user_name_type", "#{userNameType,jdbcType=INTEGER}");
        }
        
        if (record.getUserName() != null) {
            sql.VALUES("user_name", "#{userName,jdbcType=VARCHAR}");
        }
        
        if (record.getNickName() != null) {
            sql.VALUES("nick_name", "#{nickName,jdbcType=VARCHAR}");
        }
        
        if (record.getGender() != null) {
            sql.VALUES("gender", "#{gender,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getLastModifyTime() != null) {
            sql.VALUES("last_modify_time", "#{lastModifyTime,jdbcType=TIMESTAMP}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(UserBaseInfoDO record) {
        SQL sql = new SQL();
        sql.UPDATE("user_base_info");
        
        if (record.getUserId() != null) {
            sql.SET("user_id = #{userId,jdbcType=BIGINT}");
        }
        
        if (record.getUserNameType() != null) {
            sql.SET("user_name_type = #{userNameType,jdbcType=INTEGER}");
        }
        
        if (record.getUserName() != null) {
            sql.SET("user_name = #{userName,jdbcType=VARCHAR}");
        }
        
        if (record.getNickName() != null) {
            sql.SET("nick_name = #{nickName,jdbcType=VARCHAR}");
        }
        
        if (record.getGender() != null) {
            sql.SET("gender = #{gender,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getLastModifyTime() != null) {
            sql.SET("last_modify_time = #{lastModifyTime,jdbcType=TIMESTAMP}");
        }
        
        sql.WHERE("id = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }
}