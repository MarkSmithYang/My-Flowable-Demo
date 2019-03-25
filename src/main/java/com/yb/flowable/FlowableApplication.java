package com.yb.flowable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@EnableEurekaClient//针对eureka的,一般用下面比较推荐一点,省心
@EnableDiscoveryClient//这个是发现服务采用其他注册中心的服务
@SpringBootApplication
public class FlowableApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowableApplication.class, args);
    }
}
