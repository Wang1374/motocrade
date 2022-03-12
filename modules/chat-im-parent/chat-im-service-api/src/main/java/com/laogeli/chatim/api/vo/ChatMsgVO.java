package com.laogeli.chatim.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * description: chat-netty-websocket com.xunke.vo
 *
 * @author HongPei
 * @date 2021/5/29 16:12
 */
@Data
public class ChatMsgVO implements Serializable {


    //聊天类型 1:单聊 2:群聊
    private Integer chatType;

    /**
     * 消息id
     */
    private Integer msgId;

    /**
     * 聊天信息类型
     */
    private String msgType;

    /**
     * 聊天内容
     */
    private String content;

    /**
     * 发送方ID
     */
    private String senderId;

    /**
     * 接收方ID / 接收群ID
     */
    private String receiverId;

    /*
     * 会话id
     * */
    private String sessionId;
//
//    /**
//     * 消息时间
//     */
//    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /*
     * 图片路径
     * */
    private String picturePath;

}
