package com.laogeli.chatim.api.model;

import lombok.Data;

import java.util.Date;

/**
 * description: 群人员关系表
 *
 * @author HongPei
 * @date 2021/6/10 10:28
 */
@Data
public class GroupUsers {

    /**
     * 主键自增id
     */
    private Integer id;

    /*
     * 群ID
     * */
    private Integer groupId;

    /*
     * 群成员uid
     * */
    private String userId;

    /*
     * android端最后拉取群消息id
     * */
    private Integer androidLastAckMsgId;

    /*
     * ios端最后拉取群消息id
     * */
    private Integer iosLastAckMsgId;

    /*
     * web端最后拉取群消息id
     * */
    private Integer webLastAckMsgId;

    /*
     * win端最后拉取消息id
     * */
    private Integer winLastAckMsgId;

    /*
     * mac端最后拉取消息id
     * */
    private Integer macLastAckMsgId;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    private Date modifyDate;

    /**
     * 删除状态 0：未删除 1：删除
     */
    private Integer isDelete;
}
