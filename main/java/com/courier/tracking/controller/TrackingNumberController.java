package main.java.com.courier.tracking.controller;

import main.java.com.courier.tracking.services.TrackingNumberGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@RestController
@RequestMapping("/api/tracking")
class TrackingNumberController {

    @Autowired
    private TrackingNumberGeneratorService trackingNumberGeneratorService;

    @GetMapping("/generate")
    public String generateTrackingNumber(
            @RequestParam(value = "origin_country_id") String originCountryId,
            @RequestParam(value = "destination_country_id") String destinationCountryId,
            @RequestParam(value = "weight") String weight,
            @RequestParam(value = "created_at") String createdAt,
            @RequestParam(value = "customer_id") String customerId,
            @RequestParam(value = "customer_name") String customerName,
            @RequestParam(value = "customer_slug") String customerSlug) {
        try {
            //Basic validation of input parameters.
            validateInput(originCountryId, destinationCountryId, weight, createdAt, customerId);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage(); // Simplistic error handling for now.
        }

        return trackingNumberGeneratorService.generateTrackingNumber(originCountryId, destinationCountryId, weight, createdAt, customerId, customerName, customerSlug).toString();
    }

    private void validateInput(String originCountryId, String destinationCountryId, String weight, String createdAt, String customerId) {
        if (originCountryId == null || originCountryId.length() != 2) {
            throw new IllegalArgumentException("origin_country_id must be a 2-character ISO 3166-1 alpha-2 code.");
        }
        if (destinationCountryId == null || destinationCountryId.length() != 2) {
            throw new IllegalArgumentException("destination_country_id must be a 2-character ISO 3166-1 alpha-2 code.");
        }
        try {
            Double.parseDouble(weight);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("weight must be a valid number.");
        }
        try {
            OffsetDateTime.parse(createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("created_at must be in RFC 3339 format.");
        }
        try{
            UUID.fromString(customerId);
        } catch(IllegalArgumentException e){
            throw new IllegalArgumentException("customer_id must be a valid UUID");
        }

    }
}
