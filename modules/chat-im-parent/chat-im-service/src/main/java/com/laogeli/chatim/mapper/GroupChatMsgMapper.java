package com.laogeli.chatim.mapper;

import com.laogeli.chatim.api.model.GroupChatMsg;
import com.laogeli.chatim.api.vo.ChatMsgVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * description: hongpei-work com.xunke.chat.mapper
 *
 * @author HongPei
 * @date 2021/6/10 17:31
 */
@Mapper
@Repository
public interface GroupChatMsgMapper {

    int save(GroupChatMsg groupChatMsg);

    List<ChatMsgVO> findGroupMsg(Integer groupId, Integer msgId);
}
