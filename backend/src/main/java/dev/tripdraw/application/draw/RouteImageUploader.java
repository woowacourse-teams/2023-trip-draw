package dev.tripdraw.application.draw;

import static dev.tripdraw.exception.draw.DrawExceptionType.IMAGE_SAVE_FAIL;

import dev.tripdraw.exception.draw.DrawException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Component;

@Component
public class RouteImageUploader {

    private static final String ROUTE_IMAGE_FORMAT = "png";

    public String upload(BufferedImage bufferedImage) {
        String imageName = generateImageName();
        File file = new File(imageName);
        try {
            ImageIO.write(bufferedImage, ROUTE_IMAGE_FORMAT, file);
            return imageName;
        } catch (IOException e) {
            throw new DrawException(IMAGE_SAVE_FAIL);
        }
    }

    private String generateImageName() {
        return UUID.randomUUID() + "." + ROUTE_IMAGE_FORMAT;
    }
}
