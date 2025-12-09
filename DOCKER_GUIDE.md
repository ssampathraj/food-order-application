# Docker Deployment Guide

## Prerequisites
- Docker Desktop installed and running
- Docker Compose (included with Docker Desktop)

## Services Overview

The application consists of the following microservices running in Docker:

| Service | Port | Description |
|---------|------|-------------|
| Eureka Server | 8761 | Service Registry for service discovery |
| Config Server | 8888 | Centralized configuration management |
| Auth Service | 8087 | Authentication & JWT authorization |
| Order Service | 8080 | Order processing |
| User Service | 8082 | User management |
| Delivery Service | 8081 | Delivery management |
| Email Service | 8083 | Email notifications |
| API Gateway | 8090 | API Gateway (routes to 8080 internally) |

## Quick Start

### 1. Build and Start All Services
```bash
docker-compose up -d
```

This command will:
- Build Docker images for all services
- Start all containers
- Create the microservices network

### 2. Check Service Status
```bash
docker-compose ps
```

### 3. View Logs
View all logs:
```bash
docker-compose logs -f
```

View logs for specific service:
```bash
docker-compose logs -f order-service
```

### 4. Access Services

- **Eureka Dashboard**: http://localhost:8761/
- **Config Server**: http://localhost:8888/
- **API Gateway**: http://localhost:8090/
- **Order Service**: http://localhost:8080/
- **User Service**: http://localhost:8082/
- **Auth Service**: http://localhost:8087/
- **Delivery Service**: http://localhost:8081/
- **Email Service**: http://localhost:8083/

## Common Docker Compose Commands

### Stop Services
```bash
docker-compose down
```

### Stop and Remove Volumes
```bash
docker-compose down -v
```

### Rebuild Images
```bash
docker-compose build --no-cache
```

### Rebuild and Start Specific Service
```bash
docker-compose up -d --build order-service
```

### Scale a Service
```bash
docker-compose up -d --scale order-service=3
```

### Restart Services
```bash
docker-compose restart
```

## Troubleshooting

### Services Not Starting
1. Check Docker is running: `docker ps`
2. View service logs: `docker-compose logs <service-name>`
3. Check port availability

### Service Discovery Issues
- Ensure all services are on the same network: `docker network ls`
- Check Eureka dashboard to verify service registration

### Port Conflicts
If ports are already in use, modify the port mappings in `docker-compose.yml`

### Build Failures
- Clean build: `docker-compose build --no-cache`
- Check Java/Maven versions in Dockerfile

## Network Communication

All services communicate through the `microservices-network` bridge network. Services can reach each other using their container names (e.g., `http://order-service:8080`)

## Environment Variables

Services use Docker environment variables for configuration. Key variables:
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE`: Eureka server location
- `EUREKA_INSTANCE_HOSTNAME`: Service hostname in Eureka
- `SPRING_CLOUD_CONFIG_URI`: Config server location
- `JAVA_OPTS`: JVM options

## Health Checks

Each service includes a health check endpoint. Docker will automatically restart unhealthy containers.

To manually check service health:
```bash
docker-compose exec order-service curl http://localhost:8080/actuator/health
```

## Production Considerations

For production deployment:
1. Use external databases instead of H2
2. Implement proper secret management
3. Use container registries (Docker Hub, AWS ECR, etc.)
4. Implement proper logging and monitoring
5. Use Kubernetes for orchestration
6. Configure proper resource limits
7. Implement backup and recovery strategies
