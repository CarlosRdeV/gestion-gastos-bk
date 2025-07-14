package com.carlosrdev.gestiongastos.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartupInfoRunner implements CommandLineRunner {

    private final Environment environment;

    @Value("${server.port}")
    private String serverPort;

    public StartupInfoRunner(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        String[] activeProfiles = environment.getActiveProfiles();
        String profileInfo = activeProfiles.length > 0 ? String.join(", ", activeProfiles) : "default";
        
        log.info("=================================================");
        log.info("Gestion Gastos Application Started Successfully");
        log.info("Active Profile: {}", profileInfo);
        log.info("Server Port: {}", serverPort);
        log.info("Application URL: http://localhost:{}", serverPort);
        log.info("=================================================");
    }
}