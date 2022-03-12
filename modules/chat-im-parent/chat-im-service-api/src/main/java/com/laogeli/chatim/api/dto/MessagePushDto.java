package com.laogeli.chatim.api.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * description: hongpei-work com.xunke.chat.dto
 *
 * @author HongPei
 * @date 2021/6/8 9:44
 */
@Data
public class MessagePushDto {

/*    @NotNull(message = "消息发送者编号不能为空")
    private String sendMessageUserId;*/

    @NotNull(message = "消息接受者编号不能为空")
    private String receiverId;

    @NotNull(message = "发送消息内容不能为空")
    private String content;

    /**
     * 标题
     */
    private String title;

    /**
     * 业务类型
     */
    private Integer businessType;

}
