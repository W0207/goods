package cn.edu.xmu.groupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad", "cn.edu.xmu.groupon"})
@MapperScan("cn.edu.xmu.groupon.mapper")
@EnableDiscoveryClient
public class GrouponServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrouponServiceApplication.class, args);
    }

}
