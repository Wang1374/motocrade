package com.laogeli.chatim.service;


import com.laogeli.chatim.api.model.ChatMsg;
import com.laogeli.chatim.api.vo.ChatMsgVO;

import java.util.List;

/**
 * description: hongpei-work com.xunke.chat.service
 *
 * @author HongPei
 * @date 2021/6/10 17:08
 */
public interface ChatMsgService {

    int saveMsg(ChatMsg chatMsg);

//    List<ChatMsgVO> findOffLineMsgByMsgId(String userId, String friendId, Integer msgId);
}
