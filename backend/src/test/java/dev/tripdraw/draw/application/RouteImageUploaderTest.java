package dev.tripdraw.draw.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import dev.tripdraw.draw.application.RouteImageUploader;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RouteImageUploaderTest {

    @Test
    void 파일을_업로드한다() {
        // given
        BufferedImage bufferedImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
        String domain = "https://tripdraw.site";
        String base = "/image";
        String route = "/route-images/";
        RouteImageUploader routeImageUploader = new RouteImageUploader(domain, base, route);

        // expect
        try (MockedStatic<ImageIO> imageIO = Mockito.mockStatic(ImageIO.class)) {
            String imageUrl = routeImageUploader.upload(bufferedImage);

            imageIO.verify(
                    () -> ImageIO.write(any(BufferedImage.class), any(String.class), any(File.class)),
                    times(1)
            );
        }
    }

    @Test
    void 파일의_위치를_반환한다() {
        // given
        BufferedImage bufferedImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
        String domain = "https://tripdraw.site";
        String base = "/image";
        String route = "/route-images/";
        RouteImageUploader routeImageUploader = new RouteImageUploader(domain, base, route);

        // expect
        try (MockedStatic<ImageIO> imageIO = Mockito.mockStatic(ImageIO.class)) {
            String imageUrl = routeImageUploader.upload(bufferedImage);

            assertThat(imageUrl).startsWith(domain + route);
        }
    }
}
