package com.liuxin;

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
import java.util.Scanner;

public class Ollama {
    private static final String CHAT_URL = "http://127.0.0.1:11434/api/chat";

    public static void main(String[] args) {
        Ollama gptClient = new Ollama();
        Scanner scanner = new Scanner(System.in);

        System.out.println("欢迎使用 GPT 聊天！输入 'exit' 退出。");

        while (true) {
            System.out.print("你: ");
            String userInput = scanner.nextLine();

            // 检查用户是否输入 'exit' 来退出
            if ("exit".equalsIgnoreCase(userInput)) {
                System.out.println("再见！(*^_^*)");
                break;
            }

            try {
                // 调用方法发送消息给GPT，并实时接收返回的流式数据
                System.out.print("GPT: ");
                gptClient.sendStreamMessage(userInput);
                System.out.println(); // 输出完毕后换行
            } catch (Exception e) {
                System.err.println("GPT调用失败: " + e.getMessage());
            }
        }

        scanner.close();
    }

    public void sendStreamMessage(String message) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        Map<String, Object> messages = new HashMap<>();
        messages.put("model", "qwen2.5:7b");
        messages.put("messages", new Object[]{Map.of("role", "user", "content", message)});

        String requestBody = new Gson().toJson(messages);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(CHAT_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        if (response.statusCode() == 200) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    JsonObject jsonObject = JsonParser.parseString(line).getAsJsonObject();
                    if (jsonObject.has("message")) {
                        String content = jsonObject.getAsJsonObject("message").get("content").getAsString();
                        System.out.print(content); // 实时显示内容
                    }
                    if (jsonObject.has("done") && jsonObject.get("done").getAsBoolean()) {
                        break;
                    }
                }
            }
        } else {
            throw new Exception("GPT请求失败，状态码：" + response.statusCode());
        }
    }
}
