package com.carlosrdev.gestiongastos.health;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@TestPropertySource(properties = {
    "management.server.port=", // Use same port as main application in tests
    "management.endpoints.web.exposure.include=*"
})
class HealthEndpointsIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getActuatorUrl(String endpoint) {
        return "http://localhost:" + port + "/actuator/" + endpoint;
    }

    @Test
    void healthEndpoint_ShouldReturnOk() {
        // When
        ResponseEntity<Map> response = restTemplate.getForEntity(
                getActuatorUrl("health"), Map.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("status")).isEqualTo("UP");
    }


    @Test
    void healthEndpoint_ShouldIncludeCustomHealthIndicators() {
        // When
        ResponseEntity<Map> response = restTemplate.getForEntity(
                getActuatorUrl("health"), Map.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, Object> body = response.getBody();
        assertThat(body.get("status")).isEqualTo("UP");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> components = (Map<String, Object>) body.get("components");
        
        // Verify custom health indicators are present
        assertThat(components).containsKey("applicationHealth");
        assertThat(components).containsKey("databaseHealth");
        
        // Check application health details
        @SuppressWarnings("unchecked")
        Map<String, Object> appHealth = (Map<String, Object>) components.get("applicationHealth");
        assertThat(appHealth.get("status")).isEqualTo("UP");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> appDetails = (Map<String, Object>) appHealth.get("details");
        assertThat(appDetails).containsKey("application");
        assertThat(appDetails).containsKey("active_profile");
        assertThat(appDetails.get("application")).isEqualTo("Gestion Gastos Backend");
        assertThat(appDetails.get("active_profile")).isEqualTo("dev");
        
        // Check database health details
        @SuppressWarnings("unchecked")
        Map<String, Object> dbHealth = (Map<String, Object>) components.get("databaseHealth");
        assertThat(dbHealth.get("status")).isEqualTo("UP");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> dbDetails = (Map<String, Object>) dbHealth.get("details");
        assertThat(dbDetails).containsKey("database");
        assertThat(dbDetails).containsKey("connection_pool");
        assertThat(dbDetails.get("database")).isEqualTo("Available");
        assertThat(dbDetails.get("connection_pool")).isEqualTo("Active");
    }

    @Test
    void infoEndpoint_ShouldBeAccessible() {
        // When
        ResponseEntity<Map> response = restTemplate.getForEntity(
                getActuatorUrl("info"), Map.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void metricsEndpoint_ShouldBeAccessible() {
        // When
        ResponseEntity<Map> response = restTemplate.getForEntity(
                getActuatorUrl("metrics"), Map.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, Object> body = response.getBody();
        assertThat(body).containsKey("names");
        
        @SuppressWarnings("unchecked")
        java.util.List<String> names = (java.util.List<String>) body.get("names");
        assertThat(names).isNotEmpty();
        assertThat(names).contains("jvm.memory.used");
        assertThat(names).contains("system.cpu.usage");
    }
}