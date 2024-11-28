package com.liuxin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Long id;
    private Long postId;
    private Long userId;
    private String authorName;
    private String content;
    private Timestamp createdAt;
    private String status;

    // Getters and Setters
}
