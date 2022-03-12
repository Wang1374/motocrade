package com.laogeli.chatim.api.model;

import lombok.Data;

import java.util.Date;

/**
 * description: 单聊记录
 *
 * @author HongPei
 * @date 2021/5/29 15:59
 */
@Data
public class ChatMsg {

    /**
     * 主键自增id 单聊id
     */
    private Integer id;

    /*
     * 发送方的用户id
     * */
    private String senderId;

    /*
     * 接受方的用户 action=1 用户id action=2群id
     * */
    private String receiverId;

    /*
     * 消息类型 0文本、1.文件、2.图片
     * */
    private Integer msgType;

    /*
     * 消息状态 0未读、1已读
     * */
    private Integer hasRead;

    /*
     * 聊天内容
     * */
    private String content;

    /*
     * 创建时间
     * */
    private Date createDate;

    /*
     * 图片路径
     * */
    private String picturePath;
}
