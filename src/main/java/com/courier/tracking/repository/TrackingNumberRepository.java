package main.java.com.courier.tracking.repository;

import main.java.com.courier.tracking.entity.TrackingNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackingNumberRepository extends JpaRepository<TrackingNumberEntity, Long> {
    TrackingNumberEntity findByTrackingNumber(String trackingNumber);
}
