package com.laogeli.distribute.api.feign.factory;

import com.laogeli.distribute.api.feign.DistributeServiceClient;
import com.laogeli.distribute.api.feign.fallback.DistributeClientFallbackImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 订单feign断路器工厂
 *
 * @author yangyu
 * @date 2020-11-17
 */
@Component
public class DistributeServiceClientFallbackFactory implements FallbackFactory<DistributeServiceClient> {

    @Override
    public DistributeServiceClient create(Throwable throwable) {
        DistributeClientFallbackImpl distributeServiceClientFallback = new DistributeClientFallbackImpl();
        distributeServiceClientFallback.setThrowable(throwable);
        return distributeServiceClientFallback;
    }
}
