package com.example.service1.service;

import com.example.service1.exception.CustomExceptions;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@Service
public class Service2Client {

    private static final Logger logger = LoggerFactory.getLogger(Service2Client.class);

    @Autowired
    private WebClient service2WebClient;

    public Map<String, Object> callService2Endpoint(String endpoint) {
        return callService2Endpoint(endpoint, null);
    }

    public Map<String, Object> callService2Endpoint(String endpoint, Map<String, String> body) {
        String requestId = UUID.randomUUID().toString();
        
        try {
            logger.debug("Calling Service 2 endpoint: {} | Request ID: {}", endpoint, requestId);
            
            WebClient.RequestHeadersSpec<?> requestSpec;
            
            if (body != null) {
                requestSpec = service2WebClient
                        .post()
                        .uri(endpoint)
                        .header("X-Request-ID", requestId)
                        .bodyValue(body);
            } else {
                requestSpec = service2WebClient
                        .post()
                        .uri(endpoint)
                        .header("X-Request-ID", requestId);
            }

            Map<String, Object> response = requestSpec
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            logger.debug("Service 2 call successful: {} | Request ID: {}", endpoint, requestId);
            return response;

        } catch (WebClientResponseException ex) {
            logger.error("Service 2 error: {} {} | Request ID: {}", ex.getStatusCode(), ex.getResponseBodyAsString(), requestId);
            
            String errorMessage = extractErrorMessage(ex.getResponseBodyAsString());
            throw new CustomExceptions.DownstreamServiceException(
                "Service 2 returned an error: " + errorMessage,
                "service2",
                ex.getStatusCode().value(),
                ex.getResponseBodyAsString()
            );
        } catch (Exception ex) {
            logger.error("Unexpected error calling Service 2: {} | Request ID: {}", ex.getMessage(), requestId, ex);
            throw new CustomExceptions.DownstreamServiceException(
                "Failed to communicate with Service 2",
                "service2",
                500,
                ex.getMessage()
            );
        }
    }

    public Map<String, Object> callService2Get(String endpoint) {
        String requestId = UUID.randomUUID().toString();
        
        try {
            logger.debug("Calling Service 2 GET endpoint: {} | Request ID: {}", endpoint, requestId);
            
            Map<String, Object> response = service2WebClient
                    .get()
                    .uri(endpoint)
                    .header("X-Request-ID", requestId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            logger.debug("Service 2 GET call successful: {} | Request ID: {}", endpoint, requestId);
            return response;

        } catch (WebClientResponseException ex) {
            logger.error("Service 2 GET error: {} {} | Request ID: {}", ex.getStatusCode(), ex.getResponseBodyAsString(), requestId);
            
            String errorMessage = extractErrorMessage(ex.getResponseBodyAsString());
            throw new CustomExceptions.DownstreamServiceException(
                "Service 2 returned an error: " + errorMessage,
                "service2",
                ex.getStatusCode().value(),
                ex.getResponseBodyAsString()
            );
        } catch (Exception ex) {
            logger.error("Unexpected error calling Service 2 GET: {} | Request ID: {}", ex.getMessage(), requestId, ex);
            throw new CustomExceptions.DownstreamServiceException(
                "Failed to communicate with Service 2",
                "service2",
                500,
                ex.getMessage()
            );
        }
    }

    private String extractErrorMessage(String responseBody) {
        try {
            JsonNode jsonNode = new com.fasterxml.jackson.databind.ObjectMapper().readTree(responseBody);
            JsonNode errorNode = jsonNode.get("error");
            if (errorNode != null && errorNode.get("message") != null) {
                return errorNode.get("message").asText();
            }
        } catch (Exception e) {
            logger.warn("Could not parse error response from Service 2: {}", responseBody);
        }
        return "Unknown error from downstream service";
    }
}