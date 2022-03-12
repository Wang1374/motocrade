package com.laogeli.distribute.api.feign;

import com.laogeli.common.core.constant.ServiceConstant;
import com.laogeli.common.feign.config.CustomFeignConfig;
import com.laogeli.distribute.api.feign.factory.DistributeServiceClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 订单服务
 *
 * @author yangyu
 * @date 2020-11-17
 */
@FeignClient(value = ServiceConstant.DISTRIBUTE_SERVICE, configuration = CustomFeignConfig.class, fallbackFactory = DistributeServiceClientFallbackFactory.class)
public interface DistributeServiceClient {
}
