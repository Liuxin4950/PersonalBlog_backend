package com.liuxin.controller;

import com.liuxin.pojo.ApiResponse;
import com.liuxin.pojo.Chat;
import com.liuxin.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Chat>>> getChatList() {
        ApiResponse<List<Chat>> response = chatService.getChatList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Chat>> setChat(@RequestBody Chat chat) {
        ApiResponse<Chat> response = chatService.setChat(chat);
        return ResponseEntity.ok(response);
    }


}
