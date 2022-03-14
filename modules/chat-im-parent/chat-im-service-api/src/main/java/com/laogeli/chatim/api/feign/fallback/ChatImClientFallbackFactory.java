package com.laogeli.chatim.api.feign.fallback;

import com.laogeli.chatim.api.feign.ChatImClient;
import com.laogeli.common.core.model.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wang
 * @Date 2021-09-17 9:39
 **/
@Slf4j
@Component
public class ChatImClientFallbackFactory implements ChatImClient {

    private Throwable throwable;

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }


    @Override
    public ResponseBean pushMessage(String employeeId, String title, String content, Integer requestType) {
        log.error("消息推送失败");
        return null;
    }
}
