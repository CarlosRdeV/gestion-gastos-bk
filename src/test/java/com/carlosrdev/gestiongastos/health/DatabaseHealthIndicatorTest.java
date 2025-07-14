package com.carlosrdev.gestiongastos.health;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatabaseHealthIndicatorTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private DatabaseMetaData metaData;

    private DatabaseHealthIndicator databaseHealthIndicator;

    @BeforeEach
    void setUp() {
        databaseHealthIndicator = new DatabaseHealthIndicator(dataSource);
    }

    @Test
    void health_ShouldReturnUp_WhenDatabaseConnectionIsValid() throws SQLException {
        // Given
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(1)).thenReturn(true);
        when(connection.getMetaData()).thenReturn(metaData);
        when(metaData.getURL()).thenReturn("jdbc:h2:mem:devdb");
        when(metaData.getDatabaseProductName()).thenReturn("H2");

        // When
        Health health = databaseHealthIndicator.health();

        // Then
        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsKey("database");
        assertThat(health.getDetails()).containsKey("connection_pool");
        assertThat(health.getDetails()).containsKey("database_url");
        assertThat(health.getDetails()).containsKey("database_product");
        
        assertThat(health.getDetails().get("database")).isEqualTo("Available");
        assertThat(health.getDetails().get("connection_pool")).isEqualTo("Active");
        assertThat(health.getDetails().get("database_url")).isEqualTo("jdbc:h2:mem:devdb");
        assertThat(health.getDetails().get("database_product")).isEqualTo("H2");
    }

    @Test
    void health_ShouldReturnDown_WhenDatabaseConnectionIsInvalid() throws SQLException {
        // Given
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(1)).thenReturn(false);

        // When
        Health health = databaseHealthIndicator.health();

        // Then
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails().get("database")).isEqualTo("Connection invalid");
    }

    @Test
    void health_ShouldReturnDown_WhenSQLExceptionOccurs() throws SQLException {
        // Given
        when(dataSource.getConnection()).thenThrow(new SQLException("Connection failed"));

        // When
        Health health = databaseHealthIndicator.health();

        // Then
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails().get("database")).isEqualTo("Unavailable");
        assertThat(health.getDetails().get("error")).isEqualTo("Connection failed");
    }

    @Test
    void health_ShouldReturnDown_WhenMetaDataAccessFails() throws SQLException {
        // Given
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(1)).thenReturn(true);
        when(connection.getMetaData()).thenThrow(new SQLException("Metadata access failed"));

        // When
        Health health = databaseHealthIndicator.health();

        // Then
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails().get("database")).isEqualTo("Unavailable");
        assertThat(health.getDetails().get("error")).isEqualTo("Metadata access failed");
    }

    @Test
    void health_ShouldHandlePostgreSQLConnection() throws SQLException {
        // Given
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(1)).thenReturn(true);
        when(connection.getMetaData()).thenReturn(metaData);
        when(metaData.getURL()).thenReturn("jdbc:postgresql://localhost:5432/gestiongastos_qa");
        when(metaData.getDatabaseProductName()).thenReturn("PostgreSQL");

        // When
        Health health = databaseHealthIndicator.health();

        // Then
        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails().get("database_url")).isEqualTo("jdbc:postgresql://localhost:5432/gestiongastos_qa");
        assertThat(health.getDetails().get("database_product")).isEqualTo("PostgreSQL");
    }
}