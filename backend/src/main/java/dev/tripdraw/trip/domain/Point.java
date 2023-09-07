package dev.tripdraw.trip.domain;

import static dev.tripdraw.trip.exception.TripExceptionType.POINT_ALREADY_HAS_POST;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import dev.tripdraw.common.entity.BaseEntity;
import dev.tripdraw.trip.exception.TripException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
@NoArgsConstructor(access = PROTECTED)
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
    private boolean hasPost;

    @Column(nullable = false)
    private LocalDateTime recordedAt;

    @JoinColumn(name = "trip_id")
    @ManyToOne
    private Trip trip;

    public Point(Double latitude, Double longitude, LocalDateTime recordedAt) {
        this(null, latitude, longitude, false, recordedAt, null);
    }

    public Point(Double latitude, Double longitude, LocalDateTime recordedAt, Trip trip) {
        this(null, latitude, longitude, false, recordedAt, trip);
    }

    public Point(Double latitude, Double longitude, boolean hasPost, LocalDateTime recordedAt) {
        this(null, latitude, longitude, hasPost, recordedAt, null);
    }

    public Point(Long id, Double latitude, Double longitude, boolean hasPost, LocalDateTime recordedAt, Trip trip) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hasPost = hasPost;
        this.recordedAt = recordedAt;
        this.trip = trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
        if (!trip.contains(this)) {
            trip.add(this);
        }
    }

    public void registerPost() {
        validateNotPosted();
        this.hasPost = true;
    }

    private void validateNotPosted() {
        if (hasPost) {
            throw new TripException(POINT_ALREADY_HAS_POST);
        }
    }
}
