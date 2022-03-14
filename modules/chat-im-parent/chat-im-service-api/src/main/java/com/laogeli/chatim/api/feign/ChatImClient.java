package com.laogeli.chatim.api.feign;

import com.laogeli.chatim.api.feign.fallback.ChatImClientFallbackFactory;
import com.laogeli.common.core.constant.ServiceConstant;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.feign.config.CustomFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 消息服务
 * @author wang
 * @Date 2021-09-17 9:36
 **/
@FeignClient(value = ServiceConstant.MESSAGE_SERVICE, configuration = CustomFeignConfig.class, fallbackFactory = ChatImClientFallbackFactory.class)
public interface ChatImClient {


    /**
     * 推送消息
     * @param employeeId
     * @param title
     * @param content
     * @param requestType
     * @return
     */
    @GetMapping("/v1/chat-im/pushMessage")
    ResponseBean pushMessage(
            @RequestParam("employeeId") String employeeId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("requestType") Integer requestType);
}
