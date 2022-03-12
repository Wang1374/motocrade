package com.laogeli.chatim.api.model;

import lombok.Data;

import java.util.Date;

/**
 * description: 群聊记录
 *
 * @author HongPei
 * @date 2021/6/10 15:53
 */
@Data
public class GroupChatMsg {

    /**
     * 主键自增id 群聊id
     */
    private Integer id;

    /*
     * 发送方的用户id
     * */
    private String senderId;

    /*
     * 群ID
     * */
    private Integer groupId;

    /*
     * 消息类型 0文本、1.文件、2.图片
     * */
    private Integer msgType;

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
