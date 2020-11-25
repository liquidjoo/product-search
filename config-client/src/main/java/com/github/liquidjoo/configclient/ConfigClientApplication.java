package com.github.liquidjoo.configclient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ConfigClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApplication.class, args);
    }

    @RestController
    @Slf4j
    public static class DefaultController {

        private ApplicationConfiguration applicationConfiguration;
        private ContextRefresher contextRefresher;

        public DefaultController(final ApplicationConfiguration applicationConfiguration, final ContextRefresher contextRefresher) {
            this.applicationConfiguration = applicationConfiguration;
            this.contextRefresher = contextRefresher;
        }

        @GetMapping("/test")
        public void get() {
            log.info("{}", applicationConfiguration.toString());

        }
    }

    @RefreshScope
    @Configuration
    @ConfigurationProperties(prefix = "local")
    @Getter
    @ToString
    public static class ApplicationConfiguration {

        private String test;
    }

}
