package com.liuxin.mapper;

import com.liuxin.pojo.Chat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {
    //返回chat聊天谢谢目录
    List<Chat> list();

    //插入聊天数据

    int insert(Chat chat);

}
