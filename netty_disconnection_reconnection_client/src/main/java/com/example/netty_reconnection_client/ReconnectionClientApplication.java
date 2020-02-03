package com.example.netty_reconnection_client;

import com.example.netty_reconnection_client.client.ReconnectClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 断线重连客户端
 * @Author Liupeiqing
 * @Date 2020/2/3 3:04
 * @Version 1.0
 */
@SpringBootApplication
public class ReconnectionClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReconnectionClientApplication.class,args);

        new ReconnectClient().connect(9000,"127.0.0.1");
    }
}
