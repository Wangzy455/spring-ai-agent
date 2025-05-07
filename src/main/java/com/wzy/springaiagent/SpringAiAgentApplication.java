package com.wzy.springaiagent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@MapperScan("com.wzy.springaiagent.mapper")
public class SpringAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiAgentApplication.class, args);
    }

}
