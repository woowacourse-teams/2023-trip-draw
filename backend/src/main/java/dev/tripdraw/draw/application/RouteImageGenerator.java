package dev.tripdraw.draw.application;

import dev.tripdraw.draw.domain.Coordinates;
import dev.tripdraw.draw.domain.Positions;
import dev.tripdraw.draw.domain.RouteImageDrawer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RouteImageGenerator {

    private static final int IMAGE_SIZE = 800;
    private static final int ROUTE_SIZE = 600;

    private final RouteImageUploader routeImageUploader;

    public String generate(
            List<Double> latitudes,
            List<Double> longitudes,
            List<Double> pointedLatitudes,
            List<Double> pointedLongitudes
    ) {
        RouteImageDrawer routeImageDrawer = RouteImageDrawer.from(IMAGE_SIZE);
        Coordinates coordinates = Coordinates.of(longitudes, latitudes);
        Coordinates pointedCoordinates = Coordinates.of(pointedLongitudes, pointedLatitudes);

        drawImage(coordinates, routeImageDrawer, pointedCoordinates);

        String imageName = routeImageUploader.upload(routeImageDrawer.bufferedImage());
        routeImageDrawer.dispose();
        return imageName;
    }

    private void drawImage(Coordinates coordinates, RouteImageDrawer routeImageDrawer, Coordinates pointedCoordinates) {
        Positions positions = coordinates.calculatePositions(ROUTE_SIZE);
        Positions alignedPositions = positions.align(IMAGE_SIZE);
        routeImageDrawer.drawLine(alignedPositions);

        List<Integer> pointedIndexes = coordinates.indexOf(pointedCoordinates);
        Positions pointedPositions = alignedPositions.getPositionsByIndexes(pointedIndexes);
        routeImageDrawer.drawPoint(pointedPositions);
    }
}
