package com.laogeli.chatim.service.impl;

import com.laogeli.chatim.api.model.ChatMsg;
import com.laogeli.chatim.api.vo.ChatMsgVO;
import com.laogeli.chatim.mapper.ChatMsgMapper;
import com.laogeli.chatim.service.ChatMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description: hongpei-work com.xunke.chat.service.impl
 *
 * @author HongPei
 * @date 2021/6/10 17:09
 */
@Slf4j
@Service
public class ChatMsgServiceImpl implements ChatMsgService {

    @Autowired
    private ChatMsgMapper chatMsgMapper;

    /**
     * 保存单聊，聊天记录
     *
     * @param chatMsg 用户id
     */
    @Override
    public int saveMsg(ChatMsg chatMsg) {
        return chatMsgMapper.saveMsg(chatMsg);
    }

//    /**
//     * 根据 userId 和 friendId 和  msgId 获取离线消息
//     *
//     * @param userId 用户id
//     * @param friendId 好友id
//     * @param msgId 消息id
//     */
//    @Override
//    public List<ChatMsgVO> findOffLineMsgByMsgId(String userId, String friendId, Integer msgId) {
//        return chatMsgMapper.findOffLineMsgByMsgId(userId, friendId, msgId);
//    }
}
