package com.example.service2.exception;

public class CustomExceptions {

    public static class ValidationException extends RuntimeException {
        private final String field;
        private final String reason;

        public ValidationException(String message, String field, String reason) {
            super(message);
            this.field = field;
            this.reason = reason;
        }

        public String getField() {
            return field;
        }

        public String getReason() {
            return reason;
        }
    }

    public static class ResourceNotFoundException extends RuntimeException {
        private final String resourceType;
        private final String resourceId;

        public ResourceNotFoundException(String message, String resourceType, String resourceId) {
            super(message);
            this.resourceType = resourceType;
            this.resourceId = resourceId;
        }

        public String getResourceType() {
            return resourceType;
        }

        public String getResourceId() {
            return resourceId;
        }
    }

    public static class BusinessLogicException extends RuntimeException {
        public BusinessLogicException(String message) {
            super(message);
        }
    }

    public static class ExternalServiceException extends RuntimeException {
        private final String serviceName;

        public ExternalServiceException(String message, String serviceName) {
            super(message);
            this.serviceName = serviceName;
        }

        public String getServiceName() {
            return serviceName;
        }
    }
}