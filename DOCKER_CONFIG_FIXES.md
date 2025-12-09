# Docker Configuration Updates - Summary

## Changes Made to Support Docker Networking

### Problem Identified
All microservices were hardcoded to use `localhost` URLs, which doesn't work in Docker because:
- Inside Docker containers, `localhost` refers to the container itself, not the host
- Services need to communicate using container names (e.g., `order-service:8080` instead of `localhost:8080`)
- Eureka service discovery was pointing to `localhost:8761` instead of the container hostname

### Solution Implemented
Updated all service configuration files to support environment variables with sensible defaults for both localhost development and Docker deployment.

## File Changes

### 1. Application Configuration Files (Application Server)

#### **server/** (Eureka Server)
**File**: `application.properties`
- Changed: `eureka.instance.hostname=localhost` 
- To: `eureka.instance.hostname=${EUREKA_INSTANCE_HOSTNAME:localhost}`

#### **AuthService/** 
**File**: `application.yaml`
- Changed: Hardcoded `localhost` URLs and `ip-address: 127.0.0.1`
- To: Environment variable support with defaults
```yaml
eureka:
  instance:
    prefer-ip-address: false
    hostname: ${EUREKA_INSTANCE_HOSTNAME:localhost}
  client:
    serviceUrl:
      defaultZone: ${EUREKA_CLIENT_SERVICEURL_DEFAULTZONE:http://localhost:8761/eureka/}
```

#### **order/** (Order Service)
**File**: `application.properties`
- Added support for service URLs via environment variables:
  - `emailService.url=${EMAIL_SERVICE_URL:http://localhost:8083/api/email}`
  - `deliveryService.url=${DELIVERY_SERVICE_URL:http://localhost:8081/api/delivery}`
  - `userService.url=${USER_SERVICE_URL:http://localhost:8082/api/user}`
- Added Eureka hostname configuration
- Changed `eureka.instance.prefer-ip-address=true` to `false`

#### **user/** (User Service)
**File**: `application.properties`
- Changed: Hardcoded Eureka URL
- To: `eureka.client.service-url.defaultZone=${EUREKA_CLIENT_SERVICEURL_DEFAULTZONE:http://localhost:8761/eureka/}`
- Added: Eureka hostname and prefer-ip-address configurations

#### **delivery/** (Delivery Service)
**File**: `application.properties`
- Added: Support for Config Server URL via `CONFIG_SERVER_URL` environment variable
- Changed: Hardcoded config server to `spring.config.import=optional:configserver:${CONFIG_SERVER_URL:http://localhost:8085}`
- Added: Eureka hostname configuration

#### **email/** (Email Service)
**File**: `application.properties`
- Changed: Hardcoded Eureka URL to use environment variables
- Added: Eureka hostname and prefer-ip-address configurations

#### **gateway/** (API Gateway)
**Files**: `application.yaml` and Eureka configuration
- Changed: Server port from hardcoded `8084` to `${SERVER_PORT:8084}`
- Changed: Eureka configuration to use environment variables
- Changed: `prefer-ip-address` from `true` to `false`
- Changed: Eureka hostname to use `${EUREKA_INSTANCE_HOSTNAME:localhost}`

#### **config-server/** (Config Server)
**File**: `application.yaml`
- Changed: Server port from hardcoded `8085` to `${SERVER_PORT:8085}`

### 2. Docker Compose Configuration
**File**: `docker-compose.yml`

#### Updated Environment Variables for All Services:

| Service | New Environment Variables |
|---------|---------------------------|
| **config-server** | `SERVER_PORT=8888` |
| **order-service** | `EMAIL_SERVICE_URL=http://email-service:8083/api/email`<br/>`DELIVERY_SERVICE_URL=http://delivery-service:8081/api/delivery`<br/>`USER_SERVICE_URL=http://user-service:8082/api/user` |
| **delivery-service** | `CONFIG_SERVER_URL=http://config-server:8888` |
| **gateway** | `SERVER_PORT=8080` |

All services now include:
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/`
- `EUREKA_INSTANCE_HOSTNAME=<service-name>`
- `EUREKA_INSTANCE_IPADDRESS=<service-name>`
- `EUREKA_INSTANCE_PREFER_IP_ADDRESS=false`

## Key Configuration Patterns

### For localhost Development (No Docker)
All defaults work without any changes:
```
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka/
EMAIL_SERVICE_URL=http://localhost:8083/api/email
```

### For Docker Environment
Docker Compose automatically sets:
```
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/
EMAIL_SERVICE_URL=http://email-service:8083/api/email
```

Container names are automatically resolved via Docker's internal DNS within the `microservices-network` bridge network.

## Important Settings Changed

### Eureka Configuration
| Setting | Old Value | New Value | Reason |
|---------|-----------|-----------|--------|
| `prefer-ip-address` | `true` | `false` | Use hostnames instead of IPs in containers |
| `ip-address` | `127.0.0.1` | Removed | Let Docker assign container address |
| `hostname` | `localhost` | Container name | Enable service discovery |

## Testing Docker Deployment

Once Docker is started, services should:

1. **Register with Eureka** - Check http://localhost:8761/
   - All services should appear as "UP"
   - Service names should match container hostnames

2. **Communicate Internally** - Order Service should reach:
   - User Service at `http://user-service:8082`
   - Delivery Service at `http://delivery-service:8081`
   - Email Service at `http://email-service:8083`

3. **Access Gateway** - http://localhost:8090/
   - All routes should work through service discovery

## Backward Compatibility

✅ All changes maintain backward compatibility:
- Default values allow localhost development without Docker
- Environment variables can be overridden in any environment
- Local development continues to work as before
- Docker deployment uses container names and bridged network

## Running the Application

### Localhost (No Docker)
```bash
# Start services locally with Maven
mvn spring-boot:run
# All services use default localhost URLs
```

### Docker
```bash
docker-compose up -d
# Services use Docker environment variables
# Container names resolved via Docker DNS
```

Both modes work without code changes - only configuration differs!
