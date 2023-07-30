package dev.tripdraw.application.draw;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

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
    void 파일을_업로드하고_파일명을_반환한다() {
        // given
        BufferedImage bufferedImage = new BufferedImage(0, 0, BufferedImage.TYPE_INT_ARGB);
        RouteImageUploader routeImageUploader = new RouteImageUploader();

        // expect
        try (MockedStatic<ImageIO> imageIO = Mockito.mockStatic(ImageIO.class)) {
            String imageUrl = routeImageUploader.upload(bufferedImage);

            assertThat(imageUrl).isNotBlank();
            imageIO.verify(
                    () -> ImageIO.write(any(BufferedImage.class), any(String.class), any(File.class)),
                    times(1)
            );
        }
    }
}
