package com.carlosrdev.gestiongastos.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

@Slf4j
@Component("applicationHealth")
public class ApplicationHealthIndicator implements HealthIndicator {

    private final Environment environment;

    public ApplicationHealthIndicator(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Health health() {
        try {
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();
            
            long maxMemory = heapMemoryUsage.getMax();
            long usedMemory = heapMemoryUsage.getUsed();
            double memoryUsagePercentage = (double) usedMemory / maxMemory * 100;

            String[] activeProfiles = environment.getActiveProfiles();
            String profileInfo = activeProfiles.length > 0 ? String.join(", ", activeProfiles) : "default";

            Health.Builder healthBuilder = Health.up()
                    .withDetail("application", "Gestion Gastos Backend")
                    .withDetail("active_profile", profileInfo)
                    .withDetail("memory_usage_percentage", String.format("%.2f%%", memoryUsagePercentage))
                    .withDetail("memory_used_mb", usedMemory / (1024 * 1024))
                    .withDetail("memory_max_mb", maxMemory / (1024 * 1024));

            if (memoryUsagePercentage > 90) {
                return healthBuilder
                        .down()
                        .withDetail("status", "High memory usage detected")
                        .build();
            }

            return healthBuilder.build();

        } catch (Exception e) {
            log.error("Application health check failed", e);
            return Health.down()
                    .withDetail("application", "Health check failed")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}