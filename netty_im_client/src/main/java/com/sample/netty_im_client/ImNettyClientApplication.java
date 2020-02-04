package com.sample.netty_im_client;

import com.sample.netty_im_client.client.NettyClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 11:22
 * @Version 1.0
 */
@SpringBootApplication
public class ImNettyClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImNettyClientApplication.class,args);

         NettyClient.start();
    }
}
