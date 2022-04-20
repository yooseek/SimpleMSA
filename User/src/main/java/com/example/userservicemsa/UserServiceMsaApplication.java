package com.example.userservicemsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceMsaApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceMsaApplication.class, args);
    }

}
