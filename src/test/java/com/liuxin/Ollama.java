package com.liuxin;

// 导入所需的Gson库和Java I/O库，用于解析JSON数据和处理输入输出流

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

// 主类 Ollama，表示GPT的客户端类
public class Ollama {
    // 定义API的URL（统一资源定位符），这里指向本地的API服务器地址
    private static final String CHAT_URL = "http://127.0.0.1:11434/api/chat";

    // 主方法，程序的入口，创建Ollama对象并调用sendStreamMessage方法发送信息
    public static void main(String[] args) {


        // 创建Ollama类的实例
        Ollama gptClient = new Ollama();
        try {
            // 调用方法发送消息给GPT，并实时接收返回的流式数据
            gptClient.sendStreamMessage("你好，今天的天气怎么样？");
        } catch (Exception e) {
            // 如果发生异常，打印错误信息
            System.err.println("GPT调用失败: " + e.getMessage());
        }
    }

    // sendStreamMessage方法，用于向API发送信息并实时接收响应
    public void sendStreamMessage(String message) throws Exception {
        // 创建HttpClient实例，用于发送HTTP请求
        HttpClient client = HttpClient.newHttpClient();

        // 构建消息请求体（JSON格式的内容），以Map形式存储数据
        Map<String, Object> messages = new HashMap<>();
        // 在请求体中定义GPT模型的名称
        messages.put("model", "qwen2.5:7b");
        // 在请求体中添加实际的用户消息，使用匿名Map表示，角色为"user"，内容为用户输入的消息
        messages.put("messages", new Object[]{Map.of("role", "user", "content", message)});

        // 将Map转换为JSON字符串，Gson库将Map对象序列化为JSON格式
        String requestBody = new Gson().toJson(messages);

        // 构建HttpRequest对象，包含请求地址、请求头、请求方法和请求体
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(CHAT_URL))  // 设置请求的URL地址
                .header("Content-Type", "application/json")  // 设置请求头，内容类型为JSON
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))  // 设置请求方法为POST，并传入请求体
                .build();  // 构建请求

        // 发送请求并接收响应，使用流式处理（InputStream）接收响应数据
        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        // 检查响应状态码是否为200（成功）
        if (response.statusCode() == 200) {
            // 使用BufferedReader包装响应的InputStream，以逐行读取数据
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()))) {
                String line;
                // 逐行读取响应数据，直到读取完毕
                while ((line = reader.readLine()) != null) {
                    // 解析每一行的JSON字符串，获取JsonObject对象
                    JsonObject jsonObject = JsonParser.parseString(line).getAsJsonObject();
                    // 检查jsonObject是否包含“message”字段
                    if (jsonObject.has("message")) {
                        // 如果包含“message”，获取消息内容并打印，模拟流式输出
                        String content = jsonObject.getAsJsonObject("message").get("content").getAsString();
                        System.out.print(content); // 实时显示内容
                    }
                    // 检查是否包含“done”字段并且值为true
                    if (jsonObject.has("done") && jsonObject.get("done").getAsBoolean()) {
                        // 如果标记为“done: true”，退出循环，表示响应流结束
                        break;
                    }
                }
            }
        } else {
            // 如果状态码不是200，抛出异常并输出状态码
            throw new Exception("GPT请求失败，状态码：" + response.statusCode());
        }
    }
}
