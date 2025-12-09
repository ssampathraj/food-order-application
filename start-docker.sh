#!/bin/bash

# Docker Setup and Start Script for Food Order Application

echo ""
echo "========================================"
echo "Food Order Application - Docker Setup"
echo "========================================"
echo ""

# Check if Docker is running
echo "Checking Docker installation..."
if ! command -v docker &> /dev/null; then
    echo ""
    echo "ERROR: Docker is not installed!"
    echo ""
    echo "Please install Docker Desktop from: https://www.docker.com/products/docker-desktop"
    echo ""
    exit 1
fi

docker ps > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Docker daemon is not running!"
    echo ""
    echo "Please start Docker Desktop and try again."
    echo ""
    exit 1
fi

echo "Docker is running. Proceeding with setup..."
echo ""

# Display services that will be started
echo "Services to be started:"
echo "- Eureka Server (8761)"
echo "- Config Server (8888)"
echo "- Auth Service (8087)"
echo "- Order Service (8080)"
echo "- User Service (8082)"
echo "- Delivery Service (8081)"
echo "- Email Service (8083)"
echo "- API Gateway (8090)"
echo ""

# Build and start containers
echo "Building images and starting containers..."
docker-compose up -d

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================"
    echo "Services are starting up..."
    echo "========================================"
    echo ""
    sleep 10
    echo ""
    echo "Checking service status..."
    docker-compose ps
    echo ""
    echo "========================================"
    echo "Service URLs:"
    echo "========================================"
    echo "Eureka Dashboard:  http://localhost:8761/"
    echo "Config Server:     http://localhost:8888/"
    echo "API Gateway:       http://localhost:8090/"
    echo "Order Service:     http://localhost:8080/"
    echo "User Service:      http://localhost:8082/"
    echo "Auth Service:      http://localhost:8087/"
    echo "Delivery Service:  http://localhost:8081/"
    echo "Email Service:     http://localhost:8083/"
    echo ""
    echo "To view logs: docker-compose logs -f"
    echo "To stop services: docker-compose down"
    echo ""
else
    echo ""
    echo "ERROR: Failed to start services!"
    echo "Check the output above for details."
    echo ""
    exit 1
fi
