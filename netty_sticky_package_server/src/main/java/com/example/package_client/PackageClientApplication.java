package com.example.package_client;

import com.example.package_client.client.PackageClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author Liupeiqing
 * @Date 2020/2/5 12:25
 * @Version 1.0
 */
@SpringBootApplication
public class PackageClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(PackageClientApplication.class,args);
        PackageClient.start();
    }
}
