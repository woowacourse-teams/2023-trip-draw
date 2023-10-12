package dev.tripdraw.draw.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import dev.tripdraw.file.application.FileUploader;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class RouteImageUploaderTest {

    @Mock
    private FileUploader fileUploader;

    @Test
    void 파일을_업로드한다() throws IOException {
        // given
        BufferedImage bufferedImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
        String root = "root/";
        String directory = "directory/";
        RouteImageUploader routeImageUploader = new RouteImageUploader(root, directory, fileUploader);

        // when
        routeImageUploader.upload(bufferedImage);

        // then
        then(fileUploader)
                .should(times(1))
                .upload(anyString(), any(MultipartFile.class));
    }

    @Test
    void 파일의_위치를_반환한다() throws IOException {
        // given
        given(fileUploader.upload(anyString(), any(MultipartFile.class)))
                .willReturn("https://domain.com/root/directory/image.png");

        BufferedImage bufferedImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
        String root = "root/";
        String directory = "directory/";
        RouteImageUploader routeImageUploader = new RouteImageUploader(root, directory, fileUploader);

        // when
        String imageUrl = routeImageUploader.upload(bufferedImage);

        // then
        assertThat(imageUrl).contains(root, directory, ".png");
    }
}
