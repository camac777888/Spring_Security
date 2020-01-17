package com.sn.Mapper;


import com.sn.Domain.Auth;
import com.sn.Domain.User;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserMapper {
    /**
     * 查詢當前用戶對象
     */
    @Select( "select * from user where username = #{value}")
    public User findByUsername(String username);

    /**
     *查詢當前用戶擁有的權限
     */
    @Select("select auth.* FROM specsec.user\n" +
            "inner join specsec.user_role on user_role.userId = user.id\n" +
            "inner join specsec.role_auth on  user_role.userId = role_auth.roleId\n" +
            "inner join specsec.auth on role_auth.authId = auth.id\n" +
            "where username =#{value}")
    public List<Auth> findAuthByUsername(String username);

@Update("update user set password =#{password} where username =#{username}")
    public void updatePassword(User user);

@Insert("INSERT INTO specsec.user (name, username, password,createDate,lastLoginDate) VALUES (#{name},#{username}, #{password},#{createDate},#{lastLoginDate});")
    public void createAccount(User user);
}
