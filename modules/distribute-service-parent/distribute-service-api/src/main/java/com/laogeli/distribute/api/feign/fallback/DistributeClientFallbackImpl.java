package com.laogeli.distribute.api.feign.fallback;

import com.laogeli.distribute.api.feign.DistributeServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 日志断路器实现
 *
 * @author wang
 * @date 2020-11-17
 */
@Slf4j
@Component
public class DistributeClientFallbackImpl implements DistributeServiceClient {

    private Throwable throwable;

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
