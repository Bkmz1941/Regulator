package ru.bkmz1994;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerRegulator {
    public static void main(String[] args) {
        try {
            SpringApplication.run(ServerRegulator.class, args);
        } catch (Throwable throwable) {
            System.out.println(throwable.getMessage());
        }
    }
}