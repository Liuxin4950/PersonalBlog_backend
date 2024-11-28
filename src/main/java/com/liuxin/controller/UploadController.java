package com.liuxin.controller;

import com.liuxin.pojo.ApiResponse;
import com.liuxin.utils.AliOSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class UploadController {
    @Autowired
    private AliOSSUtils aliOSSUtils;

    @PostMapping("api/upload")
    public ApiResponse<String> upload(MultipartFile image) throws IOException {
        try {
            System.out.println(image.getOriginalFilename());
            // 调用阿里云OSS
            String url = aliOSSUtils.upload(image);
            System.out.println("文件上传完成：" + url);
            // 使用 ApiResponse 包装返回结果
            return new ApiResponse<>(url, 200, "文件上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            // 返回错误信息
            return new ApiResponse<>(null, 500, "文件上传失败");
        }
    }
}
