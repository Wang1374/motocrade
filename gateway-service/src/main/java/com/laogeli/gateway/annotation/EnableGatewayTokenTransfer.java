package com.laogeli.gateway.annotation;

import com.laogeli.gateway.config.GatewayTokenTransferConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 因为token里带有权限信息，避免直接返回给前端，所以在网关做一层Token转换，返回给前端的只是token ID
 *
 * @author yangyu
 * @date 2019-12-31
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GatewayTokenTransferConfig.class)
public @interface EnableGatewayTokenTransfer {
}
