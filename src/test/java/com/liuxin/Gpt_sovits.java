package com.liuxin;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Gpt_sovits {
    public static void main(String[] args) {
        String urlStr = "http://127.0.0.1:5000/tts?character=流萤&text=你哈";

        try {
            // 创建连接
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            // 获取输入流
            InputStream audioStream = connection.getInputStream();

            // 配置音频格式
            AudioFormat audioFormat = new AudioFormat(32000, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);
            audioLine.open(audioFormat);
            audioLine.start();

            System.out.println("Playing audio... Press Ctrl+C to stop.");

            // 播放音频
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = audioStream.read(buffer)) != -1) {
                audioLine.write(buffer, 0, bytesRead);
            }

            // 释放资源
            audioLine.drain();
            audioLine.close();
            audioStream.close();
            System.out.println("Playback finished.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
