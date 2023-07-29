package dev.tripdraw.domain.draw;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.ToIntFunction;

public class Positions {

    private final List<Position> items;

    public Positions(List<Position> positions) {
        items = new ArrayList<>(positions);
    }

    public Positions align(int imageSize, int routeSize) {
        int xOffset = calculateOffset(Position::x, imageSize);
        int yOffset = calculateOffset(Position::y, imageSize);

        return items.stream()
                .map(item -> new Position(item.x() + xOffset, routeSize - item.y() + yOffset))
                .collect(collectingAndThen(toList(), Positions::new));
    }

    private int calculateOffset(ToIntFunction<Position> positionToInteger, int imageSize) {
        List<Integer> positions = items.stream()
                .mapToInt(positionToInteger)
                .boxed()
                .toList();

        int midValue = (Collections.min(positions) + Collections.max(positions)) / 2;
        return imageSize / 2 - midValue;
    }

    public Positions getPositionsByIndexes(List<Integer> indexes) {
        return indexes.stream()
                .map(items::get)
                .collect(collectingAndThen(toList(), Positions::new));
    }

    public int size() {
        return items.size();
    }

    public List<Integer> xPositions() {
        return items.stream()
                .mapToInt(Position::x)
                .boxed()
                .toList();
    }

    public List<Integer> yPositions() {
        return items.stream()
                .mapToInt(Position::y)
                .boxed()
                .toList();
    }

    public List<Position> items() {
        return new ArrayList<>(items);
    }
}
