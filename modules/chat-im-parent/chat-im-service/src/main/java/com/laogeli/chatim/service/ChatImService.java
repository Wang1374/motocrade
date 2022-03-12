package com.laogeli.chatim.service;


import com.laogeli.chatim.api.dto.ReceiveRequestMessageDTO;

/**
*
* @Desecription: 服务端发消息给 客户端
* @Author: yangyu
* @Date: 2021/9/3 23:08
*/
public interface ChatImService {

    void sendSystemMessage(String employeeId, String content, Integer requestType);

    void sendSystemMessage(String employeeId, String title, String content, Integer requestType);

    Boolean sendSystemMessage(ReceiveRequestMessageDTO sendMessage);
}
