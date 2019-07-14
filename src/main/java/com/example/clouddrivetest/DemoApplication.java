package com.example.clouddrivetest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner demo(final UserService userService) {
        return strings -> {
            userService.addUser("admin@admin", passwordEncoder.encode("password"), UserRole.ADMIN);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}