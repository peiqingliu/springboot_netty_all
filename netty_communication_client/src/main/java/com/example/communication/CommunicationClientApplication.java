package com.example.communication;

import com.example.communication.client.CommunicationClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 15:43
 * @Version 1.0
 */
@SpringBootApplication
public class CommunicationClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunicationClientApplication.class,args);

        CommunicationClient.start();
    }
}
