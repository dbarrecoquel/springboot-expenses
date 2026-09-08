package com.example.frontrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.example.frontrest", 
	    "com.example.expense"
	})
public class FrontRestApplication {
	public static void main(String[] args) {
        SpringApplication.run(FrontRestApplication.class, args);
    }
}