package com.example.clouddrivetest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    @Bean
    public CommandLineRunner demo(final UserService userService) {
        return strings -> {
            userService.addUser("admin@admin", "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8", UserRole.ADMIN);
            userService.addUser("user@admin", "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8", UserRole.USER);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}