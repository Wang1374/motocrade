package com.laogeli.distribute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableFeignClients(basePackages = {"com.laogeli"}) // 扫描api包里的FeignClient
@ComponentScan(basePackages = {"com.laogeli"})
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启基于方法的安全认证机制 @PreAuthorize(“hasAuthority(‘admin’)”)才会生效
@EnableCircuitBreaker //开启断路器功能
@EnableDiscoveryClient //开启服务发现客户端
@SpringBootApplication
public class DistributionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DistributionServiceApplication.class, args);
    }
}
