package main.java.com.courier.tracking.controller;

import main.java.com.courier.tracking.services.TrackingNumberGeneratorService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import java.time.format.DateTimeFormatter;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
class TrackingNumberController {

    @Autowired
    private TrackingNumberGeneratorService generator;

    /*@Autowired
    private SimpMessagingTemplate messagingTemplate;*/

    @GetMapping("/next-tracking-number")
    public ResponseEntity<Map<String, Object>> getNextTrackingNumber(
            @RequestParam("origin_country_id") String originCountryId,
            @RequestParam("destination_country_id") String destinationCountryId,
            @RequestParam("weight") String weight,
            @RequestParam("created_at") String createdAt,
            @RequestParam("customer_id") String customerId,
            @RequestParam("customer_name") String customerName,
            @RequestParam("customer_slug") String customerSlug) {

        OffsetDateTime orderCreatedAt = OffsetDateTime.parse(createdAt);

        String trackingNumber = generator.generateTrackingNumber(originCountryId, destinationCountryId, weight, orderCreatedAt, customerId, customerName, customerSlug);

        Map<String, Object> response = new HashMap<>();
        response.put("tracking_number", trackingNumber);
        response.put("created_at", orderCreatedAt.format(DateTimeFormatter.RFC_1123_DATE_TIME)); // Use RFC 3339 formatter

        // Include additional fields as requested
        response.put("origin_country_id", originCountryId);
        response.put("destination_country_id", destinationCountryId);
        response.put("weight", weight);
        response.put("customer_id", customerId);
        response.put("customer_name", customerName);
        response.put("customer_slug", customerSlug);


        //messagingTemplate.convertAndSend("/topic/tracking-number", response);


        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Invalid request parameters: " + ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Tracking number is not unique.  Please try again.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex, WebRequest request) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Internal server error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}