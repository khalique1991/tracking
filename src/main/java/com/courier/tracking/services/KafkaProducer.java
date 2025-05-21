/*
package main.java.com.courier.tracking.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "tracking-number-topic";

    public void sendTrackingNumber(String trackingNumber) {
        kafkaTemplate.send(TOPIC, trackingNumber);
    }
    public void sendTrackingNumber(Map<String, Object> response) {
        try {
            // Convert the Map to a JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(response);
            kafkaTemplate.send(TOPIC, jsonResponse);
        } catch (JsonProcessingException e) {
            // Handle the exception appropriately (e.g., log the error)
            e.printStackTrace();
        }
    }
}
*/
