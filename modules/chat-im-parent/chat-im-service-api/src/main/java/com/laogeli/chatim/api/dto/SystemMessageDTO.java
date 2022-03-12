package com.laogeli.chatim.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SystemMessageDTO {

    @NotNull(message = "消息接受者编号不能为空")
    private String receiveUserId;

    private String content;

    private String title;

    private byte role;
}
