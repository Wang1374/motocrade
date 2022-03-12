package com.laogeli.chatim.api.model;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * description: hongpei-work com.xunke.chat.dto
 *
 * @author HongPei
 * @date 2021/6/8 9:44
 */
@Data
public class MessagePush extends BaseEntity<MessagePush> {

    /**
     * 消息id
     */
    private String msgId;

    @NotNull(message = "消息接受者编号不能为空")
    private String receiverId;

    /**
     * 标题
     */
    private String title;

    @NotNull(message = "发送消息内容不能为空")
    private String content;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 消息状态 0离线消息、1在线消息
     */
    private Integer outline;

    /*
     * 创建时间
     * */
    private Date createDate;

    /**
     * 修改时间
     */
    private Date modifyDate;
}
