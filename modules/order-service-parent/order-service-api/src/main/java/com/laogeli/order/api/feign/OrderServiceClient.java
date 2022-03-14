package com.laogeli.order.api.feign;

import com.laogeli.common.core.constant.ServiceConstant;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.feign.config.CustomFeignConfig;
import com.laogeli.order.api.feign.factory.OrderServiceClientFallbackFactory;
import com.laogeli.order.api.module.Driver;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.PastOrPresent;

/**
 * 订单服务
 *
 * @author wang
 * @date 2020-06-09
 */
@FeignClient(value = ServiceConstant.ORDER_SERVICE, configuration = CustomFeignConfig.class, fallbackFactory = OrderServiceClientFallbackFactory.class)
public interface OrderServiceClient {

    /**
     * 新增默认费用参数
     *
     * @param corporateIdentify
     * @return int
     * @author wang
     * @date 2020-07-27
     */
    @PostMapping("/v1/costsSet/batchCostsSet/{corporateIdentify}")
    int batchCostsSet(@PathVariable("corporateIdentify") String corporateIdentify);

    /**
     * 新增默认订单编号
     *
     * @param corporateIdentify
     * @return int
     * @author wang
     * @date 2020-07-27
     */
    @PostMapping("/v1/orderNumber/addOrderNumber/{corporateIdentify}")
    int addOrderNumber(@PathVariable("corporateIdentify") String corporateIdentify);

    /**
     * 新增平台客户
     *
     * @param companyName
     * @return int
     * @author wang
     * @date 2020-12-22
     */
    @PostMapping("/v1/clientele/addPlatformCustomers")
    int addPlatformCustomers(@RequestParam("corporateIdentify") String corporateIdentify,
                             @RequestParam("companyName") String companyName,
                             @RequestParam("salesman") String salesman);

    /**
     * 微信授权登录  新增司机信息
     * @param driver
     * @return
     */
    @PostMapping("/v1/driver/wxDriver")
    ResponseBean<Integer> addWxDriver(@RequestBody Driver driver);
}
