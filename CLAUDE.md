# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.3 application for expense management ("gestion de gastos") built with Java 17. The backend will be deployed on AWS with OAuth authentication.

## Common Commands

### Development
- **Run application**: `./mvnw spring-boot:run` (Linux/Mac) or `mvnw.cmd spring-boot:run` (Windows)
- **Build**: `./mvnw clean package` or `mvnw.cmd clean package`
- **Run tests**: `./mvnw test` or `mvnw.cmd test`
- **Run single test**: `./mvnw test -Dtest=ClassName` or `mvnw.cmd test -Dtest=ClassName`

### Maven Wrapper
Use the Maven wrapper (`mvnw`/`mvnw.cmd`) instead of requiring global Maven installation.

## Architecture

### Technology Stack
- **Framework**: Spring Boot 3.5.3
- **Java Version**: 17
- **Build Tool**: Maven
- **Dependencies**: 
  - Spring Web (REST APIs)
  - Spring Boot DevTools (development)
  - Lombok (boilerplate reduction)
  - Spring Boot Test (testing framework)

### Package Structure
- Base package: `com.carlosrdev.gestiongastos`
- Main application class: `GestiongastosApplication.java`
- Standard Maven directory layout (`src/main/java`, `src/test/java`, `src/main/resources`)

### Configuration
- Application properties: `src/main/resources/application.properties`
- Application name: `gestiongastos`

## Development Notes

- The project uses Lombok for reducing boilerplate code
- DevTools is included for hot reloading during development
- Standard Spring Boot testing setup with `@SpringBootTest`
- Future integration planned for AWS deployment and OAuth authentication

## Code Standards

- **Professional Environment**: This is an enterprise-grade application
- **No Emojis**: Avoid emojis in code, comments, logs, or documentation
- **Professional Language**: Use formal, business-appropriate language in all text
- **Clean Code**: Follow enterprise coding standards and best practices