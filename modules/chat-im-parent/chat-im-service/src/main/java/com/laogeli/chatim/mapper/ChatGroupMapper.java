package com.laogeli.chatim.mapper;

import com.laogeli.chatim.api.model.ChatGroup;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * description: chat-netty-websocket com.xunke.mapper
 *
 * @author HongPei
 * @date 2021/5/29 16:14
 */
@Mapper
@Repository
public interface ChatGroupMapper {

    /**
     * 通过群组id获取群组信息
     *
     * @param id 参数类型
     */
    ChatGroup findChatGroupById(Integer id);

    List<ChatGroup> findChatGroup();

    int addGroup(ChatGroup chatGroup);

    int delGroup(int id);
}
