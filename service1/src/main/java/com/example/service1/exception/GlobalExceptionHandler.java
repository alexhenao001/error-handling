package com.example.service1.exception;

import com.example.service1.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomExceptions.DownstreamServiceException.class)
    public ResponseEntity<ErrorResponse> handleDownstreamServiceException(
            CustomExceptions.DownstreamServiceException ex, HttpServletRequest request) {
        String requestId = getRequestId(request);
        logger.error("Downstream service error: {} | Service: {} | Request ID: {}",
                ex.getMessage(), ex.getServiceName(), requestId);
        
        Map<String, Object> details = new HashMap<>();
        details.put("serviceName", ex.getServiceName());
        details.put("downstreamStatusCode", ex.getStatusCode());
        details.put("downstreamError", ex.getDownstreamError());
        
        ErrorResponse errorResponse = new ErrorResponse(
            "DOWNSTREAM_SERVICE_ERROR",
            ex.getMessage(),
            requestId,
            request.getRequestURI(),
            details
        );

        
        HttpStatus status = mapDownstreamStatusToUpstream(ex.getStatusCode());
        return ResponseEntity.status(status).body(errorResponse);
    }

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

    private HttpStatus mapDownstreamStatusToUpstream(int downstreamStatus) {
        return switch (downstreamStatus) {
            case 400, 422 -> HttpStatus.BAD_REQUEST;
            case 404 -> HttpStatus.NOT_FOUND;
            case 502 -> HttpStatus.BAD_GATEWAY;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}