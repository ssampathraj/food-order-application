@echo off
REM Docker Setup and Start Script for Food Order Application

echo.
echo ========================================
echo Food Order Application - Docker Setup
echo ========================================
echo.

REM Check if Docker is running
echo Checking Docker installation...
docker ps >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Docker is not running or not installed!
    echo.
    echo Please ensure:
    echo 1. Docker Desktop is installed
    echo 2. Docker Desktop is running
    echo 3. You have proper permissions to run Docker
    echo.
    echo On Windows, you may need to:
    echo - Start Docker Desktop from the Start menu
    echo - Run PowerShell as Administrator
    echo - Enable WSL 2 backend for Docker
    echo.
    pause
    exit /b 1
)

echo Docker is running. Proceeding with setup...
echo.

REM Display services that will be started
echo Services to be started:
echo - Eureka Server (8761)
echo - Config Server (8888)
echo - Auth Service (8087)
echo - Order Service (8080)
echo - User Service (8082)
echo - Delivery Service (8081)
echo - Email Service (8083)
echo - API Gateway (8090)
echo.

REM Build and start containers
echo Building images and starting containers...
docker-compose up -d

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo Services are starting up...
    echo ========================================
    echo.
    timeout /t 10
    echo.
    echo Checking service status...
    docker-compose ps
    echo.
    echo ========================================
    echo Service URLs:
    echo ========================================
    echo Eureka Dashboard:  http://localhost:8761/
    echo Config Server:     http://localhost:8888/
    echo API Gateway:       http://localhost:8090/
    echo Order Service:     http://localhost:8080/
    echo User Service:      http://localhost:8082/
    echo Auth Service:      http://localhost:8087/
    echo Delivery Service:  http://localhost:8081/
    echo Email Service:     http://localhost:8083/
    echo.
    echo To view logs: docker-compose logs -f
    echo To stop services: docker-compose down
    echo.
) else (
    echo.
    echo ERROR: Failed to start services!
    echo Check the output above for details.
    echo.
)

pause
