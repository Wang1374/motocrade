package com.laogeli.chatim.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.laogeli.chatim.api.dto.MessagePushDto;
import com.laogeli.chatim.api.model.MessagePush;
import com.laogeli.chatim.api.result.ResponseResult;
import com.laogeli.chatim.common.IMConstant;
import com.laogeli.chatim.constant.Constant;
import com.laogeli.chatim.enums.MsgActionEnum;
import com.laogeli.chatim.enums.MsgSignFlagEnum;
import com.laogeli.chatim.mapper.MessagePushMapper;
import com.laogeli.chatim.api.redis.service.RedisCacheService;
import com.laogeli.chatim.service.MessagePushService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * description: hongpei-work com.xunke.chat.service.impl
 *
 * @author HongPei
 * @date 2021/6/8 10:35
 */
@Slf4j
@Service
public class MessagePushServiceImpl implements MessagePushService {

    @Autowired
    private SendMessageServiceImpl sendMessageService;

    @Autowired
    private RedisCacheService redisCacheService;

    @Qualifier("threadPool")
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    MessagePushMapper messagePushMapper;


    /**
     * 根据接受者id推送消息
     *
     * @param messagePushDto 参数类型
     */
    @Override
    public void MessagePushById(MessagePushDto messagePushDto) {
        MessagePush messagePush = new MessagePush();
        BeanUtils.copyProperties(messagePushDto, messagePush);
        messagePush.setMsgId("11111");

        messagePush.setCreateDate(new Date());
        // 全部设置离线，待ACK 设置已读未读,一端已读便可
        messagePush.setOutline(MsgSignFlagEnum.unSign.type);
        // TODO 是否需要先判断是否有这个用户？
        // 判断用户是否在线
        // TODO 是否判断插入成功？不判断是否有并发情况？导致ACK查询空指针.而且需要优化
        if (messagePushMapper.saveMsgPush(messagePush) > 0) {
            for (String client : Constant.clients) {
                Channel toUserCtx = Constant.onlineUserMap.get(messagePushDto.getReceiverId() + "#" + client);
                if (toUserCtx == null) {
                    String key = String.format("%s-%s", IMConstant.CHAT_USER_UNREAD_CONTACT_PREFIX, messagePush.getReceiverId() + "#" + client);
                    List<String> result = redisCacheService.getList(key);
                    Set<String> repeat = new HashSet<>(result);
                    String msgStr = JSONObject.toJSONString(messagePush);
                    if (!repeat.contains(msgStr)) {
                        redisCacheService.listAdd(key, messagePush);
                    }
                    log.info("该{}端用户还没登录,已存离线消息", client);
                } else {
                    // messagePush.setOutline(MsgSignFlagEnum.signed.type);
                    String responseResult = JSONObject.toJSONString(new ResponseResult().success(MsgActionEnum.SYSTEM_PUSH.type)
                            .setData("receiverId", messagePush.getReceiverId())
                            .setData("msgId", messagePush.getMsgId())
                            .setData("title", messagePush.getTitle())
                            .setData("content", messagePush.getContent())
                            .setData("businessType", messagePush.getBusinessType())
                            .setData("createDate", messagePush.getCreateDate()));
                    sendMessageService.sendMessageToClient(toUserCtx, responseResult);
                    log.info("推送消息给:[{}],消息内容为:[{}]", toUserCtx, responseResult);
                }
            }
        }
        // 推送消息记录 放入队列 异步插入进数据库
        // 如果推送消息特别多，用队列时，消息还在队列中未执行完，此时用户上线发现没有离线消息，过啦1秒，离线消息才入库，导致用户少收一条记录
        /*threadPoolTaskExecutor.execute(() -> {
            messagePushMapper.saveMsgPush(messagePush);
        });*/
    }

    @Override
    public MessagePush findByMsgId(MessagePush messagePush) {
        return messagePushMapper.findByMsgId(messagePush);
    }

    @Override
    public int ackByMsgId(MessagePush messagePush) {
        return messagePushMapper.ackByMsgId(messagePush);
    }
}
