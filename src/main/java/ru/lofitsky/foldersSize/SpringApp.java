package ru.lofitsky.foldersSize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SpringApp {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(SpringApp.class, args);

        Process process = Runtime.getRuntime().exec("explorer http://localhost:8080/");
    }
}