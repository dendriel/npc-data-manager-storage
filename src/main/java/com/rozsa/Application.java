package com.rozsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages="com.rozsa.repository")
@EnableTransactionManagement
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
