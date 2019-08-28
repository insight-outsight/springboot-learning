package org.springbootlearning.api.dao.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.springbootlearning.api.dao.entity.UserBaseInfoDO;

public interface UserBaseInfoDOMapper {
    @Delete({
        "delete from user_base_info",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into user_base_info (id, user_id, ",
        "user_name_type, user_name, ",
        "nick_name, gender, ",
        "create_time, last_modify_time)",
        "values (#{id,jdbcType=BIGINT}, #{userId,jdbcType=BIGINT}, ",
        "#{userNameType,jdbcType=INTEGER}, #{userName,jdbcType=VARCHAR}, ",
        "#{nickName,jdbcType=VARCHAR}, #{gender,jdbcType=INTEGER}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{lastModifyTime,jdbcType=TIMESTAMP})"
    })
    int insert(UserBaseInfoDO record);

    @InsertProvider(type=UserBaseInfoDOSqlProvider.class, method="insertSelective")
    int insertSelective(UserBaseInfoDO record);

    @Select({
        "select",
        "id, user_id, user_name_type, user_name, nick_name, gender, create_time, last_modify_time",
        "from user_base_info",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results(id = "userBaseInfoResultMap", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="user_name_type", property="userNameType", jdbcType=JdbcType.INTEGER),
        @Result(column="user_name", property="userName", jdbcType=JdbcType.VARCHAR),
        @Result(column="nick_name", property="nickName", jdbcType=JdbcType.VARCHAR),
        @Result(column="gender", property="gender", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modify_time", property="lastModifyTime", jdbcType=JdbcType.TIMESTAMP)
    })
    UserBaseInfoDO selectByPrimaryKey(Long id);
    

    @Select({
        "select",
        "id, user_id, user_name_type, user_name, nick_name, gender, create_time, last_modify_time",
        "from user_base_info",
        "where user_name = #{userName,jdbcType=VARCHAR}"
    })
    @ResultMap("userBaseInfoResultMap")
    UserBaseInfoDO selectByUserName(String userName);
    

    @UpdateProvider(type=UserBaseInfoDOSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(UserBaseInfoDO record);

    @Update({
        "update user_base_info",
        "set user_id = #{userId,jdbcType=BIGINT},",
          "user_name_type = #{userNameType,jdbcType=INTEGER},",
          "user_name = #{userName,jdbcType=VARCHAR},",
          "nick_name = #{nickName,jdbcType=VARCHAR},",
          "gender = #{gender,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "last_modify_time = #{lastModifyTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UserBaseInfoDO record);
}