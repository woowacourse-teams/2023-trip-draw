package dev.tripdraw.draw.application;

import static dev.tripdraw.draw.exception.DrawExceptionType.IMAGE_SAVE_FAIL;

import dev.tripdraw.draw.exception.DrawException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RouteImageUploader {

    private static final String ROUTE_IMAGE_FORMAT = "png";

    private final String domain;
    private final String base;
    private final String route;

    public RouteImageUploader(
            @Value("${file.common.domain}") String domain,
            @Value("${file.common.root}") String base,
            @Value("${file.route.directory}") String route
    ) {
        this.domain = domain;
        this.base = base;
        this.route = route;
    }

    public String upload(BufferedImage bufferedImage) {
        String imageName = generateImageName();
        File file = new File(base + route + imageName);
        try {
            ImageIO.write(bufferedImage, ROUTE_IMAGE_FORMAT, file);
            return domain + route + imageName;
        } catch (IOException e) {
            throw new DrawException(IMAGE_SAVE_FAIL);
        }
    }

    private String generateImageName() {
        return UUID.randomUUID() + "." + ROUTE_IMAGE_FORMAT;
    }
}
