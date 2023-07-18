package dev.tripdraw.domain.trip;

import static lombok.AccessLevel.PROTECTED;

import dev.tripdraw.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Point extends BaseEntity {

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private LocalDateTime recordedAt;

    @Builder
    public Point(Double latitude, Double longitude, LocalDateTime recordedAt) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.recordedAt = recordedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return Objects.equals(latitude, point.latitude) && Objects.equals(longitude, point.longitude)
                && Objects.equals(recordedAt, point.recordedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, recordedAt);
    }

    @Override
    public String toString() {
        return "Point{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", recordedAt=" + recordedAt +
                '}';
    }
}
