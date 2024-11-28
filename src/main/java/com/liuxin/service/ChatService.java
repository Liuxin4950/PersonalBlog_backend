package com.liuxin.service;

import com.liuxin.mapper.ChatMapper;
import com.liuxin.pojo.ApiResponse;
import com.liuxin.pojo.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    @Autowired
    private ChatMapper chatMapper;

    public ApiResponse<List<Chat>> getChatList() {
        List<Chat> chats = chatMapper.list();
        return new ApiResponse<>(chats, 200, "成功获取聊天数据");
    }

    public ApiResponse<Chat> setChat(Chat chat) {
        int re = chatMapper.insert(chat);
        System.out.println("生成的自增 ID：" + chat.getId());
        return new ApiResponse<>(chat, 200, "添加成功！");
    }


}
