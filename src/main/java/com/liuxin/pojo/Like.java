package com.liuxin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    private Long id;
    private Long userId;
    private Long postId;
    private Timestamp createdAt;

    // Getters and Setters
}
