package com.github.liquidjoo.productsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
public class ProductSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductSearchApplication.class, args);
    }

}
