package dev.tripdraw.domain.draw;

import static dev.tripdraw.exception.draw.DrawExceptionType.INVALID_COORDINATE;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import dev.tripdraw.exception.draw.DrawException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.IntStream;

public class Coordinates {

    private final List<Coordinate> coordinates;

    private Coordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public static Coordinates of(List<Double> xValues, List<Double> yValues) {
        validate(xValues, yValues);
        return IntStream.range(0, xValues.size())
                .mapToObj(index -> new Coordinate(xValues.get(index), yValues.get(index)))
                .collect(collectingAndThen(toList(), Coordinates::new));
    }

    private static void validate(List<Double> xValues, List<Double> yValues) {
        if (xValues.size() != yValues.size()) {
            throw new DrawException(INVALID_COORDINATE);
        }
    }

    public List<Position> calculatePositions(Integer routeImageSize) {
        List<Double> xValues = mapCoordinatesToValues(Coordinate::x);
        List<Double> yValues = mapCoordinatesToValues(Coordinate::y);

        return toPositions(xValues, yValues, routeImageSize);
    }

    private List<Double> mapCoordinatesToValues(ToDoubleFunction<Coordinate> coordinateToDoubleFunction) {
        return coordinates.stream()
                .mapToDouble(coordinateToDoubleFunction)
                .boxed()
                .toList();
    }

    private List<Position> toPositions(List<Double> xValues, List<Double> yValues, Integer routeImageSize) {
        Double maxDifference = calculateMaxDifference(xValues, yValues);

        List<Integer> xPositions = toSinglePositions(xValues, maxDifference, routeImageSize);
        List<Integer> yPositions = toSinglePositions(yValues, maxDifference, routeImageSize);

        List<Position> positions = new ArrayList<>();
        for (int index = 0; index < xPositions.size(); index++) {
            Position position = new Position(xPositions.get(index), yPositions.get(index));
            positions.add(position);
        }
        return positions;
    }

    private Double calculateMaxDifference(List<Double> xValues, List<Double> yValues) {
        double xDifference = Collections.max(xValues) - Collections.min(xValues);
        double yDifference = Collections.max(yValues) - Collections.min(yValues);

        return Math.max(xDifference, yDifference);
    }

    private List<Integer> toSinglePositions(List<Double> values, Double maxDifference, Integer routeImageSize) {
        Double minValue = Collections.min(values);
        return values.stream()
                .map(value -> normalizeCoordinate(value, maxDifference, minValue))
                .map(value -> mapToGrid(value, routeImageSize))
                .toList();
    }

    private double normalizeCoordinate(Double coordinate, Double maxDifference, Double minValue) {
        return (coordinate - minValue) / maxDifference;
    }

    private int mapToGrid(Double coordinate, Integer graphSize) {
        return (int) (coordinate * graphSize);
    }
}
