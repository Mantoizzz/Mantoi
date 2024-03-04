package com.forum.mantoi.sys.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.forum.mantoi.sys.dao.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author DELL
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select(value = "select * from mantoi.t_user where email = #{email} limit 1")
    User findByEmail(@Param("email") String email);

    @Select(value = "select * from mantoi.t_user where username = #{username} limit 1;")
    User findByUsername(@Param("username") String username);

}
