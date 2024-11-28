package com.liuxin.mapper;

import com.liuxin.pojo.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper {
    // 注册
    @Insert("INSERT INTO users (username, password, email, phone, image_url) VALUES (#{username}, #{password}, #{email}, #{phone}, #{imageUrl})")
    int insert(User user);

    // 根据 id 查询用户
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);

    // 根据用户名查找用户
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    // 根据手机号查找用户
    @Select("SELECT * FROM users WHERE phone = #{phone}")
    User findByPhone(String phone);

    // 根据邮箱查找用户
    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);

    // 查找用户列表
    @Select("SELECT * FROM users")
    List<User> list();

    // 根据手机号查找用户
    @Select("SELECT COUNT(*) FROM users WHERE phone = #{phone}")
    int countByPhone(String phone);

    // 根据用户名查找用户
    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    int countByUsername(String username);

    // 根据邮箱查找用户
    @Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
    int countByEmail(String email);

    // 更新用户最后登录时间
    @Update("UPDATE users SET last_login = #{lastLogin} WHERE id = #{id}")
    void updateLastLogin(@Param("id") Long id, @Param("lastLogin") LocalDateTime lastLogin);

    // 更新用户信息
    @Update("UPDATE users SET username = #{username}, email = #{email}, phone = #{phone}, " +
            "image_url = #{imageUrl}, updated_at = NOW() WHERE id = #{id}")
    int updateUser(User user);
}
