package com.liuxin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("api")
public class GptSoVITSCController {

    private static final String TTS_API_URL = "http://127.0.0.1:5000/tts";

    // 处理 TTS 请求
    @PostMapping("/tts")
    public String handleTTS(@RequestBody Map<String, Object> requestPayload) {
        try {
            // 校验请求参数
            if (requestPayload == null || !requestPayload.containsKey("text") || requestPayload.get("text").toString().trim().isEmpty()) {
                return "错误：请求参数中缺少 'text' 字段或其值为空。";
            }

            // 请求 TTS API
            byte[] audioData = requestTTS(requestPayload);
            if (audioData == null || audioData.length == 0) {
                return "错误：无法获取有效音频数据。";
            }

            // 保存音频文件到本地，并返回静态访问路径
            return saveAudioToFile(audioData);
        } catch (Exception e) {
            e.printStackTrace();
            return "错误：处理请求时出现异常，请检查服务器日志。";
        }
    }

    // 向 TTS API 发送请求并获取音频数据
    private byte[] requestTTS(Map<String, Object> requestPayload) throws IOException {
        // 创建 RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 请求体：将前端传递的参数转换为 JSON
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(requestPayload);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

        // 发送 POST 请求，获取音频字节流
        ResponseEntity<byte[]> response = restTemplate.exchange(TTS_API_URL, HttpMethod.POST, requestEntity, byte[].class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("错误：请求 TTS API 失败，HTTP 状态码: " + response.getStatusCode());
        }
    }

    // 保存音频文件到本地并返回静态路径
// 保存音频文件到 static 目录
    private String saveAudioToFile(byte[] audioData) throws Exception {
        String directoryPath = "src/main/resources/static/tts_outputs"; // 存放到 static 目录
        File directory = new File(directoryPath);

        // 检查目录是否存在，如果不存在则创建
        if (!directory.exists() && !directory.mkdirs()) {
            throw new Exception("无法创建音频保存目录，请检查权限。");
        }

        // 设置文件名并保存
        String fileName = "audio.wav";
        String filePath = directoryPath + File.separator + fileName;

        try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
            fos.write(audioData);
        }

        return "/tts_outputs/audio.wav"; // 返回相对路径
    }


}
