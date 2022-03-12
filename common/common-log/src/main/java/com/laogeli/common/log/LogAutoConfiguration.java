package com.laogeli.common.log;

import com.laogeli.common.log.aspect.LogAspect;
import com.laogeli.common.log.event.LogListener;
import com.laogeli.user.api.feign.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync // 开启多线程 配合@Async
@Configuration
@ConditionalOnWebApplication
public class LogAutoConfiguration {

    @Autowired
    private UserServiceClient userServiceClient;

    @Bean
    public LogListener sysLogListener() {
        return new LogListener(userServiceClient);
    }

    @Bean
    public LogAspect sysLogAspect() {
        return new LogAspect();
    }
}
