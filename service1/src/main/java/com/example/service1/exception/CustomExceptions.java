package com.example.service1.exception;

public class CustomExceptions {

    private CustomExceptions() {
        // Private constructor to prevent instantiation
    }

    public static class DownstreamServiceException extends RuntimeException {
        private final String serviceName;
        private final int statusCode;
        private final String downstreamError;

        public DownstreamServiceException(String message, String serviceName, int statusCode, String downstreamError) {
            super(message);
            this.serviceName = serviceName;
            this.statusCode = statusCode;
            this.downstreamError = downstreamError;
        }

        public String getServiceName() {
            return serviceName;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getDownstreamError() {
            return downstreamError;
        }
    }

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

    public static class BusinessLogicException extends RuntimeException {
        public BusinessLogicException(String message) {
            super(message);
        }
    }
}