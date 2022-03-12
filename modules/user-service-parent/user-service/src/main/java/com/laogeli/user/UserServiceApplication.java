package com.laogeli.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableDiscoveryClient //开启服务发现客户端
@EnableFeignClients(basePackages = {"com.laogeli"}) // 扫描api包里的FeignClient
@ComponentScan(basePackages = {"com.laogeli"})
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启基于方法的安全认证机制
@EnableCircuitBreaker //开启断路器功能
@EnableAsync // 开启多线程 配合@Async
//@SpringCloudApplication //可以取代上面三个
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
