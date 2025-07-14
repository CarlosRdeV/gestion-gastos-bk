package com.carlosrdev.gestiongastos.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Component("databaseHealth")
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1)) {
                return Health.up()
                        .withDetail("database", "Available")
                        .withDetail("connection_pool", "Active")
                        .withDetail("database_url", connection.getMetaData().getURL())
                        .withDetail("database_product", connection.getMetaData().getDatabaseProductName())
                        .build();
            } else {
                return Health.down()
                        .withDetail("database", "Connection invalid")
                        .build();
            }
        } catch (SQLException e) {
            log.error("Database health check failed", e);
            return Health.down()
                    .withDetail("database", "Unavailable")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}