package com.laogeli.chatim.mapper;

import com.laogeli.chatim.api.model.MessagePush;
import com.laogeli.common.core.persistence.CrudMapper;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

/**
 * description: hongpei-work com.xunke.chat.mapper
 *
 * @author HongPei
 * @date 2021/6/10 9:03
 */
@Mapper
@Repository
public interface MessagePushMapper extends CrudMapper<MessagePush> {

    int saveMsgPush(MessagePush messagePush);

    MessagePush findByMsgId(MessagePush messagePush);

    int ackByMsgId(MessagePush messagePush);
}
