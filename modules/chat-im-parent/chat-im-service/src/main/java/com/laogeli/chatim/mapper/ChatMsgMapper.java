package com.laogeli.chatim.mapper;

import com.laogeli.chatim.api.model.ChatMsg;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ChatMsgMapper {

    int saveMsg(ChatMsg chatMsg);

}
