package com.laogeli.order.api.feign.factory;

import com.laogeli.order.api.feign.OrderServiceClient;
import com.laogeli.order.api.feign.fallback.OrderServiceClientFallbackImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 订单feign断路器工厂
 *
 * @author wang
 * @date 2020-06-09
 */
@Component
public class OrderServiceClientFallbackFactory implements FallbackFactory<OrderServiceClient> {

    @Override
    public OrderServiceClient create(Throwable throwable) {
        OrderServiceClientFallbackImpl orderServiceClientFallback = new OrderServiceClientFallbackImpl();
        orderServiceClientFallback.setThrowable(throwable);
        return orderServiceClientFallback;
    }
}
