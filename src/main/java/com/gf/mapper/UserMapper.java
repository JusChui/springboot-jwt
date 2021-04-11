package com.gf.mapper;

import com.gf.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    @Select("select id , username , password, name from user where username = #{username}")
    User loadUserByUsername(@Param("username") String username);

    User loadUserByMap(Map<String, Object> params);

    /**
     * 范围查询，匹配多个学生
     *
     * @param params
     * @return
     */
    List<User> loadUsersByMap(Map<String, Object> params);

    /**
     * 向user表中插入数据
     *
     * @param params 用户信息
     * @return int
     */
    int saveUser(Map<String, Object> params);

    /**
     * 向user_role表中插入数据
     *
     * @param params 用户权限信息
     * @return int
     */
    int saveRole(Map<String, Object> params);

    int add2MyStudent(Map<String, Object> params);
}
