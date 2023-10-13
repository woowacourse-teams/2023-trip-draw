package dev.tripdraw.draw.application;

import static dev.tripdraw.draw.exception.DrawExceptionType.IMAGE_SAVE_FAIL;
import static dev.tripdraw.file.domain.FileType.IMAGE_PNG;

import dev.tripdraw.draw.exception.DrawException;
import dev.tripdraw.file.application.FileUploader;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RouteImageUploader {

    private static final String DOT = ".";

    private final String root;
    private final String directory;
    private final FileUploader fileUploader;

    public RouteImageUploader(
            @Value("${file.common.root}") String root,
            @Value("${file.route.directory}") String directory,
            FileUploader fileUploader
    ) {
        this.root = root;
        this.directory = directory;
        this.fileUploader = fileUploader;
    }

    public String upload(BufferedImage bufferedImage) {
        String fileName = UUID.randomUUID() + DOT + IMAGE_PNG.extension();
        String path = root + directory + fileName;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(bufferedImage, IMAGE_PNG.extension(), byteArrayOutputStream);
            return fileUploader.upload(path, ImageMultipartFile.of(byteArrayOutputStream, fileName));
        } catch (Exception e) {
            throw new DrawException(IMAGE_SAVE_FAIL);
        }
    }
}
