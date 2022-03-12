package com.laogeli.chatim.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * description: 群信息实体
 *
 * @author HongPei
 * @date 2021/5/29 15:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGroup {

    /**
     * 主键自增id 群组id
     */
    private Integer id;

    /**
     * 群名称
     */
    private String groupName;

    /**
     * 群公告
     */
    private String groupNote;

    /**
     * 群头像
     */
    private String groupAvatarUrl;

    /**
     * 群创建人
     */
    private String createUser;

    /**
     * 群创建时间
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
