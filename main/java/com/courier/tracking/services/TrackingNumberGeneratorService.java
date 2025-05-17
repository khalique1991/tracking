package main.java.com.courier.tracking.services;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.digest.DigestUtils;

@Service
public class TrackingNumberGeneratorService {

    private static final Pattern TRACKING_NUMBER_PATTERN = Pattern.compile("^[A-Z0-9]{1,16}$");
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // Method to generate a unique tracking number.
    public Map<String, Object> generateTrackingNumber(String originCountryId, String destinationCountryId, String weight, String createdAt, String customerId, String customerName, String customerSlug) {
        String trackingNumber;
        do {
            trackingNumber = generateTrackingNumberImpl(originCountryId, destinationCountryId, weight, createdAt, customerId, customerName, customerSlug);
        } while (!isValidTrackingNumber(trackingNumber));

        Map<String, Object> response = new HashMap<>();
        response.put("tracking_number", trackingNumber);
        response.put("created_at", OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        response.put("origin_country_id", originCountryId);
        response.put("destination_country_id", destinationCountryId);
        response.put("weight", weight);
        response.put("customer_id", customerId);
        response.put("customer_name", customerName);
        response.put("customer_slug", customerSlug);
        return response;
    }

    private String generateTrackingNumberImpl(String originCountryId, String destinationCountryId, String weight, String createdAt, String customerId, String customerName, String customerSlug) {
        StringBuilder sb = new StringBuilder();

        // Incorporate origin and destination
        sb.append(originCountryId);
        sb.append(destinationCountryId);

        // Add a customer-related part (using a hash for brevity and to stay within length limit)
        String customerHash = DigestUtils.md5Hex(customerId).substring(0, 4);  // First 4 characters of MD5 hash
        sb.append(customerHash);

        // Add some random characters
        int remainingLength = 16 - sb.length();
        for (int i = 0; i < remainingLength; i++) {
            int index = ThreadLocalRandom.current().nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString().toUpperCase();
    }

    private String generateRandomTrackingNumber() {
        //Alphanumeric characters only
        StringBuilder sb = new StringBuilder();
        int length = ThreadLocalRandom.current().nextInt(1, 17); // Length between 1 and 16
        for (int i = 0; i < length; i++) {
            int index = ThreadLocalRandom.current().nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    private boolean isValidTrackingNumber(String trackingNumber) {
        Matcher matcher = TRACKING_NUMBER_PATTERN.matcher(trackingNumber);
        return matcher.matches();
    }
}