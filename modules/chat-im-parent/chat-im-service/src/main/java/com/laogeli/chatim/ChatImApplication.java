package com.laogeli.chatim;

import com.laogeli.chatim.socket.NettyWebSocketServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.laogeli"})
@MapperScan(basePackages = {"com.laogeli.chatim.mapper"})
@EnableCircuitBreaker
public class ChatImApplication implements CommandLineRunner {

    @Autowired
    private NettyWebSocketServer nettyWebSocketServer;

    public static void main(String[] args) {
        SpringApplication.run(ChatImApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        nettyWebSocketServer.run();
    }
}
