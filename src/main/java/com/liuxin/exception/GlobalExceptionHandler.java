package com.liuxin.exception;

import com.liuxin.pojo.ApiResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 处理一般异常
    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handleGeneralException(Exception ex) {
        return new ApiResponse<>(null, 500, "服务器内部错误: " + ex.getMessage());
    }

    // 处理非法参数异常
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ApiResponse<>(null, 400, "无效的请求参数: " + ex.getMessage());
    }

    // 处理数据库异常
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<String> handleDatabaseException(DataAccessException ex) {
        return new ApiResponse<>(null, 500, "数据库操作失败: " + ex.getMessage());
    }
}
