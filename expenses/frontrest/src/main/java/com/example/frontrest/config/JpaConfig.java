package com.example.frontrest.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan({
    "com.example.expense.model"
})
@EnableJpaRepositories({
    "com.example.expense.repository"
})
public class JpaConfig {

}