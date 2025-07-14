# Gestion Gastos Backend

Backend API for expense management application built with Spring Boot. This enterprise-grade application will be deployed on AWS with OAuth authentication.

## Technology Stack

- **Framework**: Spring Boot 3.5.3
- **Java Version**: 21
- **Build Tool**: Maven
- **Database**: 
  - Development: H2 (in-memory)
  - QA/Production: PostgreSQL
- **ORM**: Spring Data JPA / Hibernate
- **Monitoring**: Spring Boot Actuator with custom health indicators
- **Additional**: Lombok, DevTools

## Project Structure

```
src/
├── main/
│   ├── java/com/carlosrdev/gestiongastos/
│   │   ├── config/           # Configuration classes
│   │   ├── controller/       # REST API controllers
│   │   ├── health/           # Custom health indicators
│   │   └── GestiongastosApplication.java
│   └── resources/
│       ├── application.properties           # Base configuration
│       ├── application-dev.properties       # Development environment
│       ├── application-qa.properties        # QA environment
│       └── application-prod.properties      # Production environment
└── test/
    └── java/com/carlosrdev/gestiongastos/
```

## Environment Configuration

### Development (dev)
- **Application Port**: 8080
- **Actuator Port**: 9090
- **Database**: H2 in-memory
- **Logging**: DEBUG level with detailed SQL logging
- **DevTools**: Enabled with auto-restart
- **H2 Console**: Available at `/h2-console`

### QA (qa)
- **Application Port**: 8081
- **Actuator Port**: 9091
- **Database**: PostgreSQL
- **Logging**: INFO level
- **DevTools**: Disabled for stability
- **Health Details**: Available with authorization

### Production (prod)
- **Application Port**: 8080
- **Actuator Port**: 9092
- **Database**: AWS RDS PostgreSQL
- **Logging**: WARN level, optimized for performance
- **DevTools**: Completely disabled
- **Security**: Enhanced with secure headers

## Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.6+ (or use included wrapper)
- PostgreSQL (for QA/Production environments)

### Running the Application

#### Development Environment (Default)
```bash
# Using Maven wrapper (recommended)
./mvnw spring-boot:run

# Using system Maven
mvn spring-boot:run
```

#### Specific Environment
```bash
# QA Environment
./mvnw spring-boot:run -Dspring.profiles.active=qa

# Production Environment
./mvnw spring-boot:run -Dspring.profiles.active=prod
```

### Build Commands

```bash
# Clean and compile
./mvnw clean compile

# Run tests
./mvnw test

# Run specific test
./mvnw test -Dtest=ClassName

# Package application
./mvnw clean package

# Skip tests during build
./mvnw clean package -DskipTests
```

## Application URLs

### Development Environment
- **Application**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
- **Health Check**: http://localhost:9090/actuator/health
- **All Actuator Endpoints**: http://localhost:9090/actuator

### QA Environment
- **Application**: http://localhost:8081
- **Health Check**: http://localhost:9091/actuator/health
- **Metrics**: http://localhost:9091/actuator/metrics
- **Prometheus**: http://localhost:9091/actuator/prometheus

### Production Environment
- **Application**: http://localhost:8080
- **Health Check**: http://localhost:9092/actuator/health (limited details)
- **Info**: http://localhost:9092/actuator/info

## Monitoring and Health Checks

The application includes comprehensive health monitoring through Spring Boot Actuator:

### Available Health Indicators
- **Database Health**: Monitors database connectivity and connection pool
- **Application Health**: Tracks memory usage, active profile, and system status
- **Disk Space**: Monitors available disk space
- **Default Spring Checks**: Standard Spring Boot health indicators

### Health Check Endpoints
```bash
# General health status
GET /actuator/health

# Detailed health information (dev/qa only)
GET /actuator/health?showDetails=true

# Custom health indicators
GET /actuator/health/applicationHealth
GET /actuator/health/databaseHealth

# Standard health indicators
GET /actuator/health/db
GET /actuator/health/diskSpace
```

### Metrics (dev/qa environments)
```bash
# Application metrics
GET /actuator/metrics

# JVM memory metrics
GET /actuator/metrics/jvm.memory.used

# HTTP request metrics
GET /actuator/metrics/http.server.requests
```

## API Endpoints

The application provides REST API endpoints for testing and future functionality:

### Test Endpoints
```bash
# Test endpoint - returns application status and information
GET /api/test/hello
```

**Response example:**
```json
{
  "message": "Hello from Gestion Gastos Backend API",
  "status": "Running successfully",
  "timestamp": "2025-07-14T15:30:19",
  "profile": "dev",
  "port": "8080",
  "version": "1.0.0"
}
```

## Environment Variables

### QA Environment
```bash
DB_USERNAME=gestiongastos_qa
DB_PASSWORD=your_password
```

### Production Environment
```bash
DATABASE_URL=jdbc:postgresql://your-rds-endpoint:5432/gestiongastos
DB_USERNAME=your_username
DB_PASSWORD=your_password
```

## Logging Configuration

Logs are automatically written to the `logs/` directory with different configurations per environment:

- **Development**: `logs/gestiongastos-dev.log` (10MB, 5 files)
- **QA**: `logs/gestiongastos-qa.log` (50MB, 10 files, daily rotation)
- **Production**: `logs/gestiongastos-prod.log` (100MB, 30 days retention)

## Development Guidelines

- Follow the established code standards (no emojis, professional language)
- Use Lombok for reducing boilerplate code
- Implement proper error handling and logging
- Write comprehensive tests for new features
- Use appropriate logging levels per environment
- Health checks and endpoints are covered by comprehensive unit and integration tests

## Deployment

### Local Development
The application runs with the `dev` profile by default, using H2 in-memory database.

### AWS Deployment
The application is designed for AWS deployment with:
- RDS PostgreSQL database
- Environment-specific configuration through environment variables
- Production-ready logging and monitoring
- Secure Actuator endpoints

## Support

For issues and feature requests, please refer to the project documentation or contact the development team.
