package dev.tripdraw.domain.trip;

import dev.tripdraw.exception.trip.TripException;

import java.util.List;
import java.util.stream.IntStream;

import static dev.tripdraw.exception.trip.TripExceptionType.ONE_OR_NO_POINT;
import static java.lang.Math.*;

public class RouteLength {

    private static final double AVERAGE_DISTANCE_FOR_ONE_DEGREE_DIFFERENCE_IN_LATITUDE_ON_KOREA_REGION = 111.1;

    private final double length;

    private RouteLength(List<Point> points) {
        this.length = calculateLength(points);
    }

    public static RouteLength from(List<Point> points) {
        return new RouteLength(points);
    }

    private Double calculateLength(List<Point> points) {
        if (points.isEmpty() || points.size() == 1) {
            throw new TripException(ONE_OR_NO_POINT);
        }

        return IntStream.range(0, points.size() - 1)
                .mapToDouble(i -> distanceBetween(points.get(i), points.get(i + 1)))
                .sum();
    }

    private Double distanceBetween(Point startPoint, Point endPoint) {
        double theta = startPoint.longitude() - endPoint.longitude();
        Double latitude1 = startPoint.latitude();
        Double latitude2 = endPoint.latitude();

        double unitDistance = sin(toRadians(latitude1)) * sin(toRadians(latitude2))
                + cos(toRadians(latitude1)) * cos(toRadians(latitude2)) * cos(toRadians(theta));

        return toDegrees(acos(unitDistance)) * AVERAGE_DISTANCE_FOR_ONE_DEGREE_DIFFERENCE_IN_LATITUDE_ON_KOREA_REGION;
    }

    public String lengthInKm() {
        return String.format("%.2f" + "km", length);
    }
}
