package com.carlosrdev.gestiongastos.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartupInfoRunnerTest {

    @Mock
    private Environment environment;

    private StartupInfoRunner startupInfoRunner;

    @BeforeEach
    void setUp() {
        startupInfoRunner = new StartupInfoRunner(environment);
        ReflectionTestUtils.setField(startupInfoRunner, "serverPort", "8080");
    }

    @Test
    void run_ShouldLogStartupInformation_WithDevProfile() throws Exception {
        // Given
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        // When
        startupInfoRunner.run();

        // Then
        verify(environment).getActiveProfiles();
        // Note: We can't easily verify log output in unit tests without additional logging framework setup
        // The main verification is that the method executes without throwing exceptions
    }

    @Test
    void run_ShouldLogStartupInformation_WithMultipleProfiles() throws Exception {
        // Given
        when(environment.getActiveProfiles()).thenReturn(new String[]{"qa", "test"});

        // When
        startupInfoRunner.run();

        // Then
        verify(environment).getActiveProfiles();
    }

    @Test
    void run_ShouldLogStartupInformation_WithNoActiveProfiles() throws Exception {
        // Given
        when(environment.getActiveProfiles()).thenReturn(new String[]{});

        // When
        startupInfoRunner.run();

        // Then
        verify(environment).getActiveProfiles();
    }

    @Test
    void run_ShouldHandleProductionProfile() throws Exception {
        // Given
        when(environment.getActiveProfiles()).thenReturn(new String[]{"prod"});
        ReflectionTestUtils.setField(startupInfoRunner, "serverPort", "8080");

        // When
        startupInfoRunner.run();

        // Then
        verify(environment).getActiveProfiles();
    }
}