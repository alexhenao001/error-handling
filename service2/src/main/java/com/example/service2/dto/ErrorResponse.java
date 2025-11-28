package com.example.service2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {
    
    @JsonProperty("error")
    private ErrorDetails error;

    public ErrorResponse() {}

    public ErrorResponse(String code, String message, String requestId, String path) {
        this.error = new ErrorDetails(code, message, requestId, path);
    }

    public ErrorResponse(String code, String message, String requestId, String path, Map<String, Object> details) {
        this.error = new ErrorDetails(code, message, requestId, path, details);
    }

    public ErrorDetails getError() {
        return error;
    }

    public void setError(ErrorDetails error) {
        this.error = error;
    }

    public static class ErrorDetails {
        private String code;
        private String message;
        private LocalDateTime timestamp;
        private String requestId;
        private String path;
        private Map<String, Object> details;

        public ErrorDetails() {}

        public ErrorDetails(String code, String message, String requestId, String path) {
            this.code = code;
            this.message = message;
            this.timestamp = LocalDateTime.now();
            this.requestId = requestId;
            this.path = path;
        }

        public ErrorDetails(String code, String message, String requestId, String path, Map<String, Object> details) {
            this(code, message, requestId, path);
            this.details = details;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Map<String, Object> getDetails() {
            return details;
        }

        public void setDetails(Map<String, Object> details) {
            this.details = details;
        }
    }
}