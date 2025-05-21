package main.java.com.courier.tracking.entity;
import java.time.OffsetDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "tracking_numbers")
public class TrackingNumberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String trackingNumber;

    public TrackingNumberEntity() {
    }

    public TrackingNumberEntity(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
}

