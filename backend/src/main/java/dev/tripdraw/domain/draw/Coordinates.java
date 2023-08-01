package dev.tripdraw.domain.draw;

import static dev.tripdraw.exception.draw.DrawExceptionType.INVALID_COORDINATES;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import dev.tripdraw.exception.draw.DrawException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.IntStream;

public class Coordinates {

    private final List<Coordinate> items;

    private Coordinates(List<Coordinate> coordinates) {
        items = new ArrayList<>(coordinates);
    }

    public static Coordinates of(List<Double> xValues, List<Double> yValues) {
        validate(xValues, yValues);
        return IntStream.range(0, xValues.size())
                .mapToObj(index -> new Coordinate(xValues.get(index), yValues.get(index)))
                .collect(collectingAndThen(toList(), Coordinates::new));
    }

    private static void validate(List<Double> xValues, List<Double> yValues) {
        if (xValues == null || yValues == null || xValues.size() != yValues.size()) {
            throw new DrawException(INVALID_COORDINATES);
        }
    }

    public Positions calculatePositions(Integer routeImageSize) {
        List<Double> xValues = mapCoordinatesToValues(Coordinate::x);
        List<Double> yValues = mapCoordinatesToValues(Coordinate::y);

        Double maxDifference = calculateMaxDifference(xValues, yValues);

        List<Integer> xPositions = toPositions(xValues, maxDifference, routeImageSize);
        List<Integer> yPositions = toPositions(yValues, maxDifference, routeImageSize);

        return IntStream.range(0, items.size())
                .mapToObj(index -> new Position(xPositions.get(index), yPositions.get(index)))
                .collect(collectingAndThen(toList(), Positions::new));
    }

    private List<Double> mapCoordinatesToValues(ToDoubleFunction<Coordinate> coordinateToDoubleFunction) {
        return items.stream()
                .mapToDouble(coordinateToDoubleFunction)
                .boxed()
                .toList();
    }

    private Double calculateMaxDifference(List<Double> xValues, List<Double> yValues) {
        double xDifference = Collections.max(xValues) - Collections.min(xValues);
        double yDifference = Collections.max(yValues) - Collections.min(yValues);

        return Math.max(xDifference, yDifference);
    }

    private List<Integer> toPositions(List<Double> values, Double maxDifference, Integer routeImageSize) {
        Double minValue = Collections.min(values);
        return values.stream()
                .map(value -> normalizeCoordinate(value, maxDifference, minValue))
                .map(value -> mapToPosition(value, routeImageSize))
                .toList();
    }

    private double normalizeCoordinate(Double coordinate, Double maxDifference, Double minValue) {
        return (coordinate - minValue) / maxDifference;
    }

    private int mapToPosition(Double coordinate, Integer routeImageSize) {
        return (int) (coordinate * routeImageSize);
    }

    public List<Integer> indexOf(Coordinates other) {
        return other.items.stream()
                .map(items::indexOf)
                .toList();
    }

    public List<Coordinate> items() {
        return new ArrayList<>(items);
    }
}
