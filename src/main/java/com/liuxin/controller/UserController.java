package com.liuxin.controller;

import com.liuxin.pojo.ApiResponse;
import com.liuxin.pojo.LoginRequest;
import com.liuxin.pojo.User;
import com.liuxin.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        ApiResponse<List<User>> response = userService.getUserList();
        return ResponseEntity.ok(response);
    }

    // 注册
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody User user) {
        ApiResponse<User> response = userService.register(user);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // 登录接口
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<User>> login(@RequestBody LoginRequest loginRequest) {
        ApiResponse<User> response = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.status(response.getCode()).body(response);
    }


    // 查询用户信息
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        ApiResponse<User> response = userService.getUserById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // 搜索用户
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<User>> getUserByUsername(@RequestParam String username) {
        ApiResponse<User> response = userService.getUserByUsername(username);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    //跟更新接口
    // 更新用户信息接口
    @PutMapping()
    public ResponseEntity<ApiResponse<User>> updateUser(@RequestBody User user) {
        ApiResponse<User> response = userService.updateUser(user);
        return ResponseEntity.status(response.getCode()).body(response);
    }

}
