package dev.tripdraw.application.draw;

import static dev.tripdraw.exception.draw.DrawExceptionType.IMAGE_SAVE_FAIL;

import dev.tripdraw.exception.draw.DrawException;
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

    @Value("${trip.domain}")
    private String domain;

    @Value("${trip.base}")
    private String base;

    @Value("${trip.route}")
    private String route;

    public String upload(BufferedImage bufferedImage) {
        String imageName = generateImageName();
        File file = new File(imageName);
        try {
            ImageIO.write(bufferedImage, ROUTE_IMAGE_FORMAT, file);
            return domain + imageName;
        } catch (IOException e) {
            throw new DrawException(IMAGE_SAVE_FAIL);
        }
    }

    private String generateImageName() {
        String baseName = base + route;
        String routeImageName = UUID.randomUUID() + "." + ROUTE_IMAGE_FORMAT;
        return baseName + routeImageName;
    }
}
