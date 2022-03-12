package com.laogeli.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableFeignClients(basePackages = {"com.laogeli"}) // 扫描api包里的FeignClient
@ComponentScan(basePackages = {"com.laogeli"})
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启基于方法的安全认证机制 @PreAuthorize(“hasAuthority(‘admin’)”)才会生效
@EnableCircuitBreaker // 开启断路器功能
@EnableDiscoveryClient // 开启服务发现客户端
@EnableScheduling // 开启机制
@SpringBootApplication
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
