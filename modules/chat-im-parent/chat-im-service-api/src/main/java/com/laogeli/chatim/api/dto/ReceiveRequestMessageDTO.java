package com.laogeli.chatim.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReceiveRequestMessageDTO {

    /*
    * 消息接收类型不能为空 业务类型 101：下单，102 做箱
    * */
    @NotNull(message = "消息接收类型不能为空")
    private Integer requestType;


    private String requestBody;
}
