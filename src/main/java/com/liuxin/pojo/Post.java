package com.liuxin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private Long id;
    private Long authorId;
    private Long categoryId;
    private String title;
    private String content;
    private String summary;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String status;
    private int viewCount;
    private String imageUrl;
    private String categoryName;
    private String categoryDescription;   // 分类描述
}
