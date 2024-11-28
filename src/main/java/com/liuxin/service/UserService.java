package com.liuxin.service;

import com.liuxin.mapper.UserMapper;
import com.liuxin.pojo.ApiResponse;
import com.liuxin.pojo.User;
import com.liuxin.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    // 注册方法
    public ApiResponse<User> register(User user) {
        // 检查用户名是否存在
        if (userMapper.findByUsername(user.getUsername()) != null) {
            return new ApiResponse<>(null, 400, "用户名已存在");
        }
        // 检查电话号码是否存在
        if (userMapper.findByPhone(user.getPhone()) != null) {
            return new ApiResponse<>(null, 400, "电话号码已存在");
        }
        // 检查电子邮件是否存在
        if (userMapper.findByEmail(user.getEmail()) != null) {
            return new ApiResponse<>(null, 400, "电子邮件已在使用");
        }

        // 插入用户
        userMapper.insert(user);
        return new ApiResponse<>(user, 200, "用户注册成功");
    }

    // 登录方法
    public ApiResponse<User> login(String username, String password) {
        // 从数据库查找用户
        User user = userMapper.findByUsername(username);
        // 登录成功，生成令牌，下发令牌
        if (user != null) {
            System.out.println(user);
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId()); // 从User对象中获取id
            claims.put("name", user.getUsername()); // 从User对象中获取id
            // 生成token
            String jwt = JwtUtils.generateJwt(claims);
            user.setToken(jwt);
        }
        if (user != null && password.equals(user.getPassword())) {
            // 登录成功，更新最后登录时间
            user.setLastLogin(LocalDateTime.now());
            userMapper.updateLastLogin(user.getId(), user.getLastLogin());

            return new ApiResponse<>(user, 200, "登录成功");
        } else {
            return new ApiResponse<>(null, 401, "无效的用户名或密码");
        }
    }

    // 查询个人信息
    public ApiResponse<User> getUserById(Long id) {
        User user = userMapper.findById(id);
        if (user != null) {
            return new ApiResponse<>(user, 200, "用户信息获取成功");
        } else {
            return new ApiResponse<>(null, 404, "用户未找到");
        }
    }

    // 搜索用户
    public ApiResponse<User> getUserByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if (user != null) {
            return new ApiResponse<>(user, 200, "用户信息获取成功");
        } else {
            return new ApiResponse<>(null, 404, "用户未找到");
        }
    }

    // 获取所有用户
    public ApiResponse<List<User>> getUserList() {
        List<User> users = userMapper.list();
        return new ApiResponse<>(users, 200, "获取用户列表成功");
    }

    //更新
    public ApiResponse<User> updateUser(User user) {
        // 检查用户名是否重复
        if (userMapper.countByUsername(user.getUsername()) > 0) {
            return new ApiResponse<>(null, 400, "用户名已存在");
        }

        // 检查邮箱是否重复
        if (userMapper.countByEmail(user.getEmail()) > 0) {
            return new ApiResponse<>(null, 400, "邮箱已存在");
        }

        // 检查手机号是否重复
        if (userMapper.countByPhone(user.getPhone()) > 0) {
            return new ApiResponse<>(null, 400, "电话号码已存在");
        }

        // 更新用户信息
        int rowsAffected = userMapper.updateUser(user);
        if (rowsAffected > 0) {
            return new ApiResponse<>(user, 200, "用户信息更新成功");
        } else {
            return new ApiResponse<>(null, 404, "用户信息更新失败");
        }
    }


}
