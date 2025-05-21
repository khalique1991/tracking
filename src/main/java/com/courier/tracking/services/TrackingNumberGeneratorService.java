package main.java.com.courier.tracking.services;

import main.java.com.courier.tracking.entity.TrackingNumberEntity;
import main.java.com.courier.tracking.repository.TrackingNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class TrackingNumberGeneratorService {
    @Autowired
    private TrackingNumberRepository repository;

    private static final Pattern TRACKING_NUMBER_PATTERN = Pattern.compile("^[A-Z0-9]{1,16}$");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Transactional
    @Cacheable(value = "trackingNumbers", key = "#originCountryId + '-' + #destinationCountryId + '-' + #customerId") // Add caching
    public String generateTrackingNumber(String originCountryId, String destinationCountryId, String weight, OffsetDateTime createdAt, String customerId, String customerName, String customerSlug) {
        // 1. Validate input parameters
        if (!isValidCountryCode(originCountryId) || !isValidCountryCode(destinationCountryId) ||
                !isValidWeight(weight) || createdAt == null || !isValidUUID(customerId) ||
                customerName == null || customerName.isEmpty() || customerSlug == null || customerSlug.isEmpty()) {
            throw new IllegalArgumentException("Invalid input parameters");
        }

        // 2. Generate the tracking number
        String datePart = DATE_FORMATTER.format(createdAt);
        String uniquePart = generateUniqueAlphanumeric(8);

        String trackingNumber = "TN" + datePart + originCountryId + destinationCountryId + uniquePart;


        // 3. Ensure uniqueness using the database
        if (repository.findByTrackingNumber(trackingNumber) != null) {
            // Handle the extremely rare case of a duplicate.  Retry with a new unique part.
            uniquePart = generateUniqueAlphanumeric(8);
            trackingNumber = "TN" + datePart + originCountryId + destinationCountryId + uniquePart;
            if (repository.findByTrackingNumber(trackingNumber) != null){
                throw new RuntimeException("Failed to generate a unique tracking number after multiple attempts.");
            }
        }

        // 4. Validate the generated tracking number against the regex pattern
        if (!TRACKING_NUMBER_PATTERN.matcher(trackingNumber).matches()) {
            throw new RuntimeException("Generated tracking number does not match the required pattern.");
        }

        TrackingNumberEntity entity = new TrackingNumberEntity(trackingNumber);
        repository.save(entity);

        return trackingNumber;
    }

    private boolean isValidCountryCode(String countryCode) {
        return countryCode != null && countryCode.length() == 2 && countryCode.matches("[A-Z]{2}");
    }

    private boolean isValidWeight(String weight) {
        if (weight == null) {
            return false;
        }
        try {
            double parsedWeight = Double.parseDouble(weight);
            return parsedWeight >= 0 && weight.matches("\\d+(\\.\\d{1,3})?");
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidUUID(String uuid) {
        if (uuid == null) {
            return false;
        }
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private String generateUniqueAlphanumeric(int length) {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
}
