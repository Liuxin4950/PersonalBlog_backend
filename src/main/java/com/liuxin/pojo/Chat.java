package com.liuxin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    private Integer id;
    private String name;
    private String text;
    private String audioPath;
    private LocalDateTime timestamp = LocalDateTime.now();

}
