package com.laogeli.order.api.feign.fallback;


import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.order.api.feign.OrderServiceClient;
import com.laogeli.order.api.module.Driver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 日志断路器实现
 *
 * @author wang
 * @date 2020-06-09
 */
@Slf4j
@Component
public class OrderServiceClientFallbackImpl implements OrderServiceClient {

    private Throwable throwable;

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    /**
     * 新增默认费用参数
     *
     * @param corporateIdentify
     * @return int
     */
    @Override
    public int batchCostsSet(String corporateIdentify) {
        log.error("feign 插入默认费用参数失败:{}, {}, {}, {}", corporateIdentify, throwable);
        return 0;
    }

    /**
     * 插入默认
     *
     * @param corporateIdentify
     * @return int
     */
    @Override
    public int addOrderNumber(String corporateIdentify) {
        log.error("feign 插入默认订单编号失败:{}, {}, {}, {}", corporateIdentify, throwable);
        return 0;
    }

    /**
     * 新增平台客户
     *
     * @param companyName
     * @return int
     */
    @Override
    public int addPlatformCustomers(String corporateIdentify, String companyName, String salesman) {
        log.error("feign 新增平台客户失败:{}, {}, {}, {}", companyName, throwable);
        return 0;
    }

    /**
     * 新增微信  司机用户失败
     * @param driver
     * @return
     */
    @Override
    public ResponseBean<Integer> addWxDriver(Driver driver) {
        log.error("feign 新增微信司机失败:{}", driver);
        return new ResponseBean<>();
    }
}
