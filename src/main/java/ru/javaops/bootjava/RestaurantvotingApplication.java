package ru.javaops.bootjava;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class RestaurantvotingApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantvotingApplication.class, args);
    }
}
