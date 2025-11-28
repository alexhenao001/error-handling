# Error Handling Microservices Demo

A comprehensive proof of concept demonstrating enterprise-grade error handling across a microservices architecture with Angular frontend and Spring Boot backends.

## Architecture Overview

This project showcases proper error handling and propagation across three tiers:

- **Angular Frontend** (Port 4200) - User interface with HTTP interceptor for error handling
- **Service 1** (Port 8081) - Upstream microservice that proxies requests to Service 2
- **Service 2** (Port 8082) - Downstream microservice with various error scenarios

```
Frontend (Angular) → Service 1 (Spring Boot) → Service 2 (Spring Boot)
     ↑                        ↑                        ↑
Error Interceptor      Global Exception Handler  Global Exception Handler
```

## Key Features

### ✨ Standardized Error Response Format
```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "User-friendly error message",
    "timestamp": "2025-11-27T10:30:00Z", 
    "requestId": "uuid-v4",
    "path": "/api/users",
    "details": {
      "field": "email",
      "reason": "Invalid email format"
    }
  }
}
```

### 🔗 Request Correlation
- End-to-end request tracing with correlation IDs
- Proper error propagation from Service 2 → Service 1 → Frontend
- Structured logging with request IDs for debugging

### 🎯 Error Scenarios Demonstrated
- **Validation Errors** - Invalid input data
- **Business Logic Errors** - Application rule violations  
- **Resource Not Found** - Missing entities
- **External Service Errors** - Downstream dependencies
- **Internal Server Errors** - Unexpected failures

## Prerequisites

- **Java 17+** - For Spring Boot services
- **Maven 3.6+** - For building Java services
- **Node.js 18+** - For Angular frontend
- **npm** - Package manager for frontend

## Quick Start

### 1. Start Service 2 (Downstream)
```bash
cd service2
mvn spring-boot:run
```
✅ Service 2 will start on **http://localhost:8082**

### 2. Start Service 1 (Upstream)
```bash
cd service1
mvn spring-boot:run
```
✅ Service 1 will start on **http://localhost:8081**

### 3. Start Angular Frontend
```bash
cd frontend/error-handling-frontend
npm install
npm start
```
✅ Frontend will start on **http://localhost:4200**

## Testing the Demo

### 🌐 Web Interface Testing
Open your browser to **http://localhost:4200** and test:

#### Error Scenario Buttons:
- **Success Response** - Verify happy path works
- **Validation Error** - Test input validation 
- **Not Found Error** - Test resource lookup failures
- **Business Logic Error** - Test business rule violations
- **External Service Error** - Test downstream service failures
- **Internal Server Error** - Test unexpected errors

#### Interactive Forms:
1. **User Validation Form** - Test with invalid/valid email formats
2. **Order Processing Form** - Test end-to-end validation flow

### 🔧 API Testing (Direct Backend)
Test the APIs directly using curl:

#### Service 1 Endpoints:
```bash
# Success case
curl http://localhost:8081/api/v1/proxy/success

# Error propagation from Service 2
curl http://localhost:8081/api/v1/proxy/validation-error

# Local Service 1 errors
curl http://localhost:8081/api/v1/local-business-error

# Complex form processing
curl -X POST http://localhost:8081/api/v1/process-order \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","name":"John Doe","productId":"PROD123"}'
```

#### Service 2 Direct Testing:
```bash
# Direct service 2 access
curl http://localhost:8082/api/v1/success
curl http://localhost:8082/api/v1/validation-error
curl http://localhost:8082/api/v1/business-error
```

## Error Handling Implementation Details

### Spring Boot Services
- **@ControllerAdvice** global exception handlers
- **Custom exception classes** for different error types
- **Request correlation ID tracking** via headers
- **Structured logging** with MDC/request context
- **HTTP status code mapping** following REST conventions

### Angular Frontend  
- **HTTP Interceptor** for global error handling
- **Modern @if control flow** syntax (Angular 17+)
- **Structured error display** with correlation IDs
- **Loading states and user feedback**

### Error Propagation Flow
1. **Frontend** makes request with `X-Request-ID` header
2. **Service 1** receives request, forwards to Service 2 
3. **Service 2** processes and may throw errors
4. **Service 1** catches Service 2 errors, wraps in `DownstreamServiceException`
5. **Service 1** global handler converts to standardized format
6. **Frontend** interceptor catches HTTP errors and displays them

## Project Structure

```
error-handling/
├── service1/                 # Upstream Spring Boot service
│   ├── src/main/java/com/example/service1/
│   │   ├── controller/       # REST endpoints
│   │   ├── service/          # Service 2 client
│   │   ├── exception/        # Global exception handling
│   │   └── dto/              # Error response DTOs
│   └── pom.xml
├── service2/                 # Downstream Spring Boot service  
│   ├── src/main/java/com/example/service2/
│   │   ├── controller/       # Error demo endpoints
│   │   ├── exception/        # Global exception handling
│   │   └── dto/              # Error response DTOs
│   └── pom.xml
├── frontend/error-handling-frontend/  # Angular frontend
│   ├── src/app/
│   │   ├── services/         # API service
│   │   ├── models/           # Error response models
│   │   ├── interceptors/     # HTTP error interceptor
│   │   └── app.*             # Main component
│   └── package.json
└── README.md
```

## Configuration

### Ports
- **Frontend**: 4200
- **Service 1**: 8081  
- **Service 2**: 8082

### CORS
Service 1 is configured to accept requests from `http://localhost:4200`

### Logging
Both services log at DEBUG level for `com.example.*` packages to show request flow

## Troubleshooting

### Services Won't Start
```bash
# Check if ports are in use
lsof -ti:8081,8082,4200

# Kill processes on ports if needed  
lsof -ti:8081,8082,4200 | xargs kill -9
```

### Frontend Loading Issues
1. Hard refresh browser (Cmd+Shift+R / Ctrl+Shift+R)
2. Check browser console for errors (F12)
3. Ensure all services are running
4. Verify no CORS errors in network tab

### Maven Build Issues
```bash
# Clean and reinstall dependencies
mvn clean install
```

### Angular Build Issues  
```bash
# Clear npm cache and reinstall
cd frontend/error-handling-frontend
rm -rf node_modules package-lock.json
npm install
```

## What You'll Learn

This demo showcases:

✅ **Enterprise Error Handling Patterns**
- Centralized exception handling with @ControllerAdvice
- Standardized error response formats
- Request correlation and tracing

✅ **Microservice Communication**
- Service-to-service error propagation
- HTTP client error handling with WebClient
- Timeout and retry strategies

✅ **Frontend Error Handling**
- HTTP interceptors for global error handling
- User-friendly error display
- Modern Angular patterns and syntax

✅ **Observability**
- Structured logging with correlation IDs
- Error tracking across service boundaries
- Request flow visibility

This comprehensive demo provides a solid foundation for implementing robust error handling in production microservices architectures.