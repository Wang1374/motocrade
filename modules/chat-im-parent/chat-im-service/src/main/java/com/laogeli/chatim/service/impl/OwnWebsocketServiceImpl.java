package com.laogeli.chatim.service.impl;

import com.alibaba.fastjson.JSON;
import com.laogeli.chatim.api.dto.MessagePushDto;
import com.laogeli.chatim.api.dto.ReceiveRequestMessageDTO;
import com.laogeli.chatim.api.dto.SystemMessageDTO;
import com.laogeli.chatim.service.ChatImService;
import com.laogeli.chatim.service.MessagePushService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;


/**
 * description: 系统消息推送 TODO 加IChatImService
 *
 * @author HongPei
 * @date 2021/6/7 17:31
 */
@Log4j2
//@DubboService(interfaceClass = ChatImService.class, protocol = {"dubbo"})
@Component
public class OwnWebsocketServiceImpl implements ChatImService {

    @Autowired
    MessagePushService messagePushService;

    @Qualifier("threadPool")
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 异步推送系统消息
     */
    @Override
    public void sendSystemMessage(String employeeId, String content, Integer requestType) {
        sendSystemMessage(employeeId, null, content, requestType);
    }

    /**
     * 异步推送系统消息 带title
     *
     * @param employeeId
     * @param title
     * @param content
     * @param requestType
     */
    @Override
    public void sendSystemMessage(String employeeId, String title, String content, Integer requestType) {
        try {
            SystemMessageDTO systemMessage = getSystemMessageDTO(employeeId, title, content);
            ReceiveRequestMessageDTO receiveRequestMessage = new ReceiveRequestMessageDTO();
            receiveRequestMessage.setRequestType(requestType);
            receiveRequestMessage.setRequestBody(JSON.toJSONString(systemMessage));
            if (!sendSystemMessage(receiveRequestMessage)) {
                log.error("sendSystemMessage失败-{}", systemMessage.getReceiveUserId() + "发送失败");
            }
        } catch (Exception e) {
            log.error("sendSystemMessage异常-{}-{}-{}-{}", employeeId, content, requestType, e.getMessage());
        }

    }

    @Override
    public Boolean sendSystemMessage(ReceiveRequestMessageDTO sendMessage) {
        try {
            SystemMessageDTO messageDTO = JSON.parseObject(sendMessage.getRequestBody(), SystemMessageDTO.class);
            //判断是否为null
           /* HttpResult result = ValidationUtils.validateEntity(messageDTO);
            if (result != null) {
                throw new IMException(result.getMessage());
            }*/
            MessagePushDto messagePushDto = new MessagePushDto();
            messagePushDto.setReceiverId(messageDTO.getReceiveUserId());
            messagePushDto.setBusinessType(sendMessage.getRequestType());
            messagePushDto.setContent(messageDTO.getContent());
            if (StringUtils.isNotEmpty(messageDTO.getTitle())) {
                messagePushDto.setTitle(messageDTO.getTitle());
            }
            log.info("sendSystemMessage-{}", JSON.toJSONString(messagePushDto));
            // 推送
            threadPoolTaskExecutor.execute(() -> {
                messagePushService.MessagePushById(messagePushDto);
            });
        } catch (Exception e) {
            log.error("sendSystemMessage异常-{}-{}", JSON.toJSONString(sendMessage), e.getMessage());
        }
        return true;
    }

    public SystemMessageDTO getSystemMessageDTO(String employeeId, String title, String content) {
        SystemMessageDTO systemMessageDTO = new SystemMessageDTO();
        if (StringUtils.isNotBlank(employeeId)) {
            systemMessageDTO.setRole((byte) 1);
            systemMessageDTO.setReceiveUserId(employeeId);
            systemMessageDTO.setContent(content);
            if (StringUtils.isNotEmpty(title)) {
                systemMessageDTO.setTitle(title);
            }
            return systemMessageDTO;
        }
        return null;
    }
}
