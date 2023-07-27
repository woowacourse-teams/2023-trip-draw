package dev.tripdraw.domain.trip;

import static jakarta.persistence.GenerationType.IDENTITY;

import dev.tripdraw.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private boolean hasPost = false;

    @Column(nullable = false)
    private LocalDateTime recordedAt;

    protected Point() {
    }

    public Point(Double latitude, Double longitude, LocalDateTime recordedAt) {
        this(null, latitude, longitude, recordedAt);
    }

    public Point(Long id, Double latitude, Double longitude, LocalDateTime recordedAt) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.recordedAt = recordedAt;
    }

    public Long id() {
        return id;
    }

    public Double latitude() {
        return latitude;
    }

    public Double longitude() {
        return longitude;
    }

    public boolean hasPost() {
        return hasPost;
    }

    public LocalDateTime recordedAt() {
        return recordedAt;
    }
}
