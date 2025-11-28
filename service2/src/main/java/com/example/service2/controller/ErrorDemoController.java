package com.example.service2.controller;

import com.example.service2.exception.CustomExceptions;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ErrorDemoController {

    @GetMapping("/success")
    public Map<String, String> success() {
        return Map.of("message", "Operation completed successfully", "service", "service2");
    }

    @GetMapping("/validation-error")
    public void triggerValidationError() {
        throw new CustomExceptions.ValidationException(
            "Invalid email format",
            "email",
            "Email must contain @ symbol"
        );
    }

    @GetMapping("/not-found")
    public void triggerNotFound() {
        throw new CustomExceptions.ResourceNotFoundException(
            "User not found",
            "User",
            "12345"
        );
    }

    @GetMapping("/business-error")
    public void triggerBusinessError() {
        throw new CustomExceptions.BusinessLogicException(
            "Account balance insufficient for this operation"
        );
    }

    @GetMapping("/external-service-error")
    public void triggerExternalServiceError() {
        throw new CustomExceptions.ExternalServiceException(
            "Payment gateway is currently unavailable",
            "payment-service"
        );
    }

    @GetMapping("/internal-error")
    public void triggerInternalError() {
        throw new RuntimeException("Unexpected database connection failure");
    }

    @PostMapping("/validate-user")
    public Map<String, String> validateUser(@RequestBody Map<String, String> user) {
        String email = user.get("email");
        String name = user.get("name");
        
        if (email == null || !email.contains("@")) {
            throw new CustomExceptions.ValidationException(
                "Invalid email format",
                "email",
                "Email must be a valid email address"
            );
        }
        
        if (name == null || name.trim().isEmpty()) {
            throw new CustomExceptions.ValidationException(
                "Name cannot be empty",
                "name",
                "Name is required"
            );
        }
        
        return Map.of("message", "User validation successful", "email", email, "name", name);
    }
}