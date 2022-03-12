package com.laogeli.chatim.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.laogeli.chatim.api.dto.ChatMsgDto;
import com.laogeli.chatim.api.model.ChatGroup;
import com.laogeli.chatim.api.model.ChatMsg;
import com.laogeli.chatim.api.model.GroupChatMsg;
import com.laogeli.chatim.api.model.GroupUsers;
import com.laogeli.chatim.api.result.ResponseResult;
import com.laogeli.chatim.api.vo.ChatMsgVO;
import com.laogeli.chatim.constant.Constant;
import com.laogeli.chatim.enums.MsgActionEnum;
import com.laogeli.chatim.enums.MsgSignFlagEnum;
import com.laogeli.chatim.mapper.ChatGroupMapper;
import com.laogeli.chatim.mapper.GroupChatMsgMapper;
import com.laogeli.chatim.service.ChatMsgService;
import com.laogeli.chatim.service.ChatService;
import com.laogeli.chatim.service.MessagePushService;
import com.laogeli.chatim.service.SendMessageService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author beifang
 * @Date 2021-09-03 12:11
 **/
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatGroupMapper chatGroupMapper;

    @Autowired
    private GroupChatMsgMapper groupChatMsgMapper;

    @Autowired
    private SendMessageService sendMessageService;

    @Qualifier("threadPool")
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private ChatMsgService chatMsgService;


    @Autowired
    private MessagePushService messagePushService;


    /**
    *
    * @Desecription: 消息推送
    * @Param:
    * @Return:
    * @Author: yangyu
    * @Date: 2021/9/3 23:10
    */
    @Override
    public void sendPush(ChatMsgDto chatMsgDto, Channel channel) {

    }

    @Override
    public void sendOne(ChatMsgDto chatMsgDto, Channel channel) {

        ChatMsg chatMsg = new ChatMsg();
        BeanUtils.copyProperties(chatMsgDto, chatMsg);
        chatMsg.setCreateDate(new Date());
        chatMsg.setHasRead(MsgSignFlagEnum.unSign.type);
        chatMsgService.saveMsg(chatMsg);
        for (String client : Constant.clients) {
            Channel toUserCtx = Constant.onlineUserMap.get(chatMsgDto.getReceiverId() + "#" + client);
            if (toUserCtx != null) {
                ChatMsgVO chatMsgVO = new ChatMsgVO();
                BeanUtils.copyProperties(chatMsg, chatMsgVO);
                chatMsgVO.setMsgId(chatMsg.getId());
                String responseResult1 = JSONObject.toJSONString(new ResponseResult()
                        .success(MsgActionEnum.SINGLE_SENDING.type)
                        .setData("data", chatMsgVO));
                sendMessageService.sendMessageToClient(toUserCtx, responseResult1);
                log.info("发送消息给:[{}],客户端为:[{}],消息内容为:[{}]", toUserCtx, client, responseResult1);
                //TODO   发送成功   senderId  删除/重新保存？

            }
            /*if (toUserCtx == null) {
                String responseJson = JSONObject.toJSONString(new ResponseResult()
                        .error(MessageFormat.format("userId为 {0} 的用户{1}没有登录，已存离线消息", chatMsgDto.getReceiverId(), client)));
                sendMessageService.sendMessageToClient(channel, responseJson);
                log.info("该用户还没登录,已存离线消息");
            } else {
                ChatMsgVO chatMsgVO = new ChatMsgVO();
                BeanUtils.copyProperties(chatMsg, chatMsgVO);
                chatMsgVO.setMsgId(chatMsg.getId());
                String responseResult = JSONObject.toJSONString(new ResponseResult()
                        .success(MsgActionEnum.SINGLE_SENDING.type)
                        .setData("data", chatMsgVO));
                sendMessageService.sendMessageToClient(toUserCtx, responseResult);
                log.info("发送消息给:[{}],客户端为:[{}],消息内容为:[{}]", toUserCtx, client, responseResult);
            }*/
        }
        // 不管是否在线，只要成功了，便发送成功回执
        String responseResult2 = JSONObject.toJSONString(new ResponseResult()
                .success(MsgActionEnum.SUCCESS.type)
                .setData("msgId", chatMsgDto.getMsgId())
                .setData("receiverId", chatMsgDto.getReceiverId())
                .setData("content", "发送消息成功"));
        sendMessageService.sendMessageToClient(channel, responseResult2);
    }

    @Override
    public void sendGroup(ChatMsgDto chatMsgDto, Channel channel) {

//        String fromUserId = chatMsgDto.getSenderId();
//        String toGroupId = chatMsgDto.getReceiverId();
//        GroupChatMsg groupChatMsg = new GroupChatMsg();
//        BeanUtils.copyProperties(chatMsgDto, groupChatMsg);
//        groupChatMsg.setGroupId(Integer.valueOf(toGroupId));
//        groupChatMsg.setCreateDate(new Date());
//        // 根据群id查询群信息 群组
//        ChatGroup chatGroup = chatGroupMapper.findChatGroupById(Integer.parseInt(toGroupId));
//        if (null == chatGroup) {
//            String responseResult = JSONObject.toJSONString(new ResponseResult().error("该群不存在！"));
//            sendMessageService.sendMessageToClient(channel, responseResult);
//        } else {
//            // 将消息存入群表
//            groupChatMsgMapper.save(groupChatMsg);
//            ChatMsgVO chatMsgVO = new ChatMsgVO();
//            BeanUtils.copyProperties(groupChatMsg, chatMsgVO);
//            chatMsgVO.setMsgId(groupChatMsg.getId());
//            String responseResult = JSONObject.toJSONString(new ResponseResult()
//                    .success(MsgActionEnum.GROUP_SENDING.type)
//                    .setData("data", chatMsgVO));
//            // TODO 根据群id去redis中查询此群下的用户数组
//            List<GroupUsers> groupUsers = groupUsersMapper.findAllByGroupId(Integer.parseInt(toGroupId));
//            log.info("查询群成员数据：[{}]", groupUsers.toString());
//            groupUsers.stream()
//                    .filter(y -> !Objects.equals(y.getUserId(), fromUserId))
//                    .forEach(x -> sendMessageService.sendMessageToClient(x.getUserId(), responseResult));
//        }
    }

    @Override
    public void ackMsg(ChatMsgDto chatMsgDto, Channel channel) {

    }

    @Override
    public void readMsg(ChatMsgDto chatMsgDto, Channel channel) {

    }

    @Override
    public void typeError(Channel channel) {

    }

    @Override
    public void loginError(Channel channel) {
        String responseResult = JSONObject.toJSONString(new ResponseResult()
                .error("请勿重复登陆！"));
        sendMessageService.sendMessageToClient(channel, responseResult);
    }
}
