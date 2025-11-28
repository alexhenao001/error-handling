package com.example.service1.controller;

import com.example.service1.exception.CustomExceptions;
import com.example.service1.service.Service2Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class ProxyController {

    @Autowired
    private Service2Client service2Client;

    @GetMapping("/proxy/success")
    public Map<String, Object> proxySuccess() {
        Map<String, Object> response = service2Client.callService2Get("/success");
        response.put("proxiedBy", "service1");
        return response;
    }

    @GetMapping("/proxy/validation-error")
    public Map<String, Object> proxyValidationError() {
        return service2Client.callService2Get("/validation-error");
    }

    @GetMapping("/proxy/not-found")
    public Map<String, Object> proxyNotFound() {
        return service2Client.callService2Get("/not-found");
    }

    @GetMapping("/proxy/business-error")
    public Map<String, Object> proxyBusinessError() {
        return service2Client.callService2Get("/business-error");
    }

    @GetMapping("/proxy/external-service-error")
    public Map<String, Object> proxyExternalServiceError() {
        return service2Client.callService2Get("/external-service-error");
    }

    @GetMapping("/proxy/internal-error")
    public Map<String, Object> proxyInternalError() {
        return service2Client.callService2Get("/internal-error");
    }

    @PostMapping("/proxy/validate-user")
    public Map<String, Object> proxyValidateUser(@RequestBody Map<String, String> user) {
        return service2Client.callService2Endpoint("/validate-user", user);
    }

    @GetMapping("/local-validation-error")
    public void localValidationError() {
        throw new CustomExceptions.ValidationException(
            "Invalid request format",
            "requestType",
            "Request type must be specified"
        );
    }

    @GetMapping("/local-business-error")
    public void localBusinessError() {
        throw new CustomExceptions.BusinessLogicException(
            "User is not authorized to perform this action"
        );
    }

    @PostMapping("/process-order")
    public Map<String, Object> processOrder(@RequestBody Map<String, Object> orderData) {
        String email = (String) orderData.get("email");
        String productId = (String) orderData.get("productId");
        
        if (email == null || email.trim().isEmpty()) {
            throw new CustomExceptions.ValidationException(
                "Email is required",
                "email",
                "Email field cannot be empty"
            );
        }
        
        if (productId == null || productId.trim().isEmpty()) {
            throw new CustomExceptions.ValidationException(
                "Product ID is required",
                "productId",
                "Product ID field cannot be empty"
            );
        }
        
        Map<String, String> userValidation = Map.of(
            "email", email,
            "name", (String) orderData.getOrDefault("name", "")
        );
        
        Map<String, Object> validationResult = service2Client.callService2Endpoint("/validate-user", userValidation);
        
        return Map.of(
            "message", "Order processed successfully",
            "orderId", "ORD-" + System.currentTimeMillis(),
            "userValidation", validationResult
        );
    }
}