package com.carlosrdev.gestiongastos.health;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationHealthIndicatorTest {

    @Mock
    private Environment environment;

    private ApplicationHealthIndicator applicationHealthIndicator;

    @BeforeEach
    void setUp() {
        applicationHealthIndicator = new ApplicationHealthIndicator(environment);
    }

    @Test
    void health_ShouldReturnUp_WhenApplicationIsHealthy() {
        // Given
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        // When
        Health health = applicationHealthIndicator.health();

        // Then
        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsKey("application");
        assertThat(health.getDetails()).containsKey("active_profile");
        assertThat(health.getDetails()).containsKey("memory_usage_percentage");
        assertThat(health.getDetails()).containsKey("memory_used_mb");
        assertThat(health.getDetails()).containsKey("memory_max_mb");
        
        assertThat(health.getDetails().get("application")).isEqualTo("Gestion Gastos Backend");
        assertThat(health.getDetails().get("active_profile")).isEqualTo("dev");
    }

    @Test
    void health_ShouldReturnDefaultProfile_WhenNoActiveProfiles() {
        // Given
        when(environment.getActiveProfiles()).thenReturn(new String[]{});

        // When
        Health health = applicationHealthIndicator.health();

        // Then
        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails().get("active_profile")).isEqualTo("default");
    }

    @Test
    void health_ShouldReturnMultipleProfiles_WhenMultipleActiveProfiles() {
        // Given
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev", "test"});

        // When
        Health health = applicationHealthIndicator.health();

        // Then
        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails().get("active_profile")).isEqualTo("dev, test");
    }

    @Test
    void health_ShouldIncludeMemoryInformation() {
        // Given
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        // When
        Health health = applicationHealthIndicator.health();

        // Then
        assertThat(health.getDetails().get("memory_usage_percentage")).isNotNull();
        assertThat(health.getDetails().get("memory_used_mb")).isNotNull();
        assertThat(health.getDetails().get("memory_max_mb")).isNotNull();
        
        // Verify memory values are numeric
        String memoryPercentage = (String) health.getDetails().get("memory_usage_percentage");
        assertThat(memoryPercentage).matches("\\d+\\.\\d{2}%");
        
        Long memoryUsed = (Long) health.getDetails().get("memory_used_mb");
        Long memoryMax = (Long) health.getDetails().get("memory_max_mb");
        assertThat(memoryUsed).isGreaterThan(0L);
        assertThat(memoryMax).isGreaterThan(0L);
        assertThat(memoryUsed).isLessThanOrEqualTo(memoryMax);
    }
}