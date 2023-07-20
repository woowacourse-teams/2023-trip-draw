package dev.tripdraw.domain.trip;

import dev.tripdraw.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class Point extends BaseEntity {

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private LocalDateTime recordedAt;

    protected Point() {
    }

    public Point(Double latitude, Double longitude, LocalDateTime recordedAt) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.recordedAt = recordedAt;
    }

    public Double latitude() {
        return latitude;
    }

    public Double longitude() {
        return longitude;
    }

    public LocalDateTime recordedAt() {
        return recordedAt;
    }
}
