package cn.edu.xmu.presale;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad", "cn.edu.xmu.presale"})
@MapperScan("cn.edu.xmu.presale.mapper")
public class PresaleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PresaleServiceApplication.class, args);
    }

}
