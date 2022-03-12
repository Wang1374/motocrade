package com.laogeli.chatim.api.dto;

import lombok.Data;

/**
 * description: chat-netty-websocket com.xunke.dto
 *
 * @author HongPei
 * @date 2021/6/1 10:17
 */
@Data
public class ChatMsgDto {

    /**
     * 动作类型 1:连接 2:单聊 3:群聊 4:ACK
     */
    private Integer action;

    /**
     * ACK类型 1:单聊 2:群聊 3:系统推送
     */
    private Integer ackType;

    /**
     * 用户登录id
     */
    private String userId;

    /**
     * 客户端类型
     */
    private String clientType;

    /**
     * 消息id
     */
    private String msgId;

    /*
     * 消息类型 0文本、1.文件、2.图片
     * */
    private Integer msgType;

    /*
     * 发送方的用户id
     * */
    private String senderId;

    /*
     * 接受方的用户 action=1 用户id action=2群id
     * */
    private String receiverId;

    /*
     * 聊天内容
     * */
    private String content;

    /*
     * 会话id
     * */
    private String sessionId;

}
