package dev.tripdraw.application.draw;

import dev.tripdraw.domain.draw.Coordinates;
import dev.tripdraw.domain.draw.Positions;
import dev.tripdraw.domain.draw.RouteImageDrawer;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RouteImageGenerator {

    private static final int IMAGE_SIZE = 800;
    private static final int ROUTE_SIZE = 600;

    private final RouteImageUploader routeImageUploader;

    public RouteImageGenerator(RouteImageUploader routeImageUploader) {
        this.routeImageUploader = routeImageUploader;
    }

    public String generate(
            List<Double> latitudes,
            List<Double> longitudes,
            List<Double> xPoints,
            List<Double> yPoints
    ) {
        RouteImageDrawer routeImageDrawer = RouteImageDrawer.from(IMAGE_SIZE);
        Coordinates coordinates = Coordinates.of(latitudes, longitudes);
        Coordinates pointCoordinates = Coordinates.of(xPoints, yPoints);

        drawImage(coordinates, routeImageDrawer, pointCoordinates);

        String imageName = routeImageUploader.upload(routeImageDrawer.bufferedImage());
        routeImageDrawer.dispose();
        return imageName;
    }

    private void drawImage(Coordinates coordinates, RouteImageDrawer routeImageDrawer, Coordinates pointCoordinates) {
        Positions alignedPositions = coordinates.calculatePositions(ROUTE_SIZE)
                .align(IMAGE_SIZE, ROUTE_SIZE);
        routeImageDrawer.drawLine(alignedPositions);

        List<Integer> pointIndexes = coordinates.indexOf(pointCoordinates);
        Positions pointPositions = alignedPositions.getPositionsByIndexes(pointIndexes);
        routeImageDrawer.drawPoint(pointPositions);
    }
}
