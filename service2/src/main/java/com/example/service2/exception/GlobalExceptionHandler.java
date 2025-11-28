package com.example.service2.exception;

import com.example.service2.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomExceptions.ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            CustomExceptions.ValidationException ex, HttpServletRequest request) {
        String requestId = getRequestId(request);
        
        Map<String, Object> details = new HashMap<>();
        details.put("field", ex.getField());
        details.put("reason", ex.getReason());
        
        ErrorResponse errorResponse = new ErrorResponse(
            "VALIDATION_ERROR",
            ex.getMessage(),
            requestId,
            request.getRequestURI(),
            details
        );
        
        logger.warn("Validation error: {} | Request ID: {}", ex.getMessage(), requestId);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            CustomExceptions.ResourceNotFoundException ex, HttpServletRequest request) {
        String requestId = getRequestId(request);
        
        Map<String, Object> details = new HashMap<>();
        details.put("resourceType", ex.getResourceType());
        details.put("resourceId", ex.getResourceId());
        
        ErrorResponse errorResponse = new ErrorResponse(
            "RESOURCE_NOT_FOUND",
            ex.getMessage(),
            requestId,
            request.getRequestURI(),
            details
        );
        
        logger.warn("Resource not found: {} | Request ID: {}", ex.getMessage(), requestId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> handleBusinessLogicException(
            CustomExceptions.BusinessLogicException ex, HttpServletRequest request) {
        String requestId = getRequestId(request);
        
        ErrorResponse errorResponse = new ErrorResponse(
            "BUSINESS_LOGIC_ERROR",
            ex.getMessage(),
            requestId,
            request.getRequestURI()
        );
        
        logger.warn("Business logic error: {} | Request ID: {}", ex.getMessage(), requestId);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternalServiceException(
            CustomExceptions.ExternalServiceException ex, HttpServletRequest request) {
        String requestId = getRequestId(request);
        
        Map<String, Object> details = new HashMap<>();
        details.put("serviceName", ex.getServiceName());
        
        ErrorResponse errorResponse = new ErrorResponse(
            "EXTERNAL_SERVICE_ERROR",
            ex.getMessage(),
            requestId,
            request.getRequestURI(),
            details
        );
        
        logger.error("External service error: {} | Request ID: {}", ex.getMessage(), requestId);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        String requestId = getRequestId(request);
        
        Map<String, Object> details = new HashMap<>();
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null) {
            details.put("field", fieldError.getField());
            details.put("reason", fieldError.getDefaultMessage());
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            "VALIDATION_ERROR",
            "Request validation failed",
            requestId,
            request.getRequestURI(),
            details
        );
        
        logger.warn("Method argument validation error | Request ID: {}", requestId);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        String requestId = getRequestId(request);
        
        ErrorResponse errorResponse = new ErrorResponse(
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred",
            requestId,
            request.getRequestURI()
        );
        
        logger.error("Unexpected error: {} | Request ID: {}", ex.getMessage(), requestId, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private String getRequestId(HttpServletRequest request) {
        String requestId = request.getHeader("X-Request-ID");
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }
        return requestId;
    }
}