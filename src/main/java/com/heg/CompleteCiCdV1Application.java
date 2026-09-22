package com.heg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CompleteCiCdV1Application extends SpringBootServletInitializer {

@Override
protected SpringApplicationBuilder configure(
        SpringApplicationBuilder application) {

    return application.sources(CompleteCiCdV1Application.class);
}

public static void main(String[] args) {
    SpringApplication.run(CompleteCiCdV1Application.class, args);
}

}
