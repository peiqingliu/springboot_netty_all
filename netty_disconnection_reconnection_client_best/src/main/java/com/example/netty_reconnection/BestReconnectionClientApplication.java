package com.example.netty_reconnection;

import com.example.netty_reconnection.client.ReconnectClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 14:23
 * @Version 1.0
 */
@SpringBootApplication
public class BestReconnectionClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(BestReconnectionClientApplication.class,args);

        new ReconnectClient().start();
    }

}
