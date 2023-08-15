package dev.tripdraw.application.draw;

import dev.tripdraw.domain.draw.Coordinates;
import dev.tripdraw.domain.draw.Positions;
import dev.tripdraw.domain.draw.RouteImageDrawer;
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
        Coordinates coordinates = Coordinates.of(latitudes, longitudes);
        Coordinates pointedCoordinates = Coordinates.of(pointedLatitudes, pointedLongitudes);

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
