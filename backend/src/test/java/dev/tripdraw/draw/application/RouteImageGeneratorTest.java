package dev.tripdraw.draw.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import dev.tripdraw.draw.application.RouteImageGenerator;
import dev.tripdraw.draw.application.RouteImageUploader;
import java.awt.image.BufferedImage;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class RouteImageGeneratorTest {

    @Test
    void 경로와_위치_점을_입력받아_경로_이미지를_생성_후_파일을_업로드한다() {
        // given
        List<Double> latitudes = List.of(
                126.96352960597338, 126.96987292787792, 126.98128481452298, 126.99360339342958,
                126.99867565340067, 127.001935378366117, 126.9831048919687, 126.97189273528845, 127.02689859997221
        );
        List<Double> longitudes = List.of(
                37.590841000217125, 37.58435564234159, 37.58594375113966, 37.58248524741927,
                37.56778118088622, 37.55985240444085, 37.548030119488665, 37.5119879225856, 37.4848859333388
        );
        List<Double> xPoints = List.of(126.96352960597338, 126.96987292787792, 126.98128481452298);
        List<Double> yPoints = List.of(37.590841000217125, 37.58435564234159, 37.58594375113966);
        RouteImageUploader routeImageUploader = mock(RouteImageUploader.class);
        RouteImageGenerator routeImageGenerator = new RouteImageGenerator(routeImageUploader);

        // when
        routeImageGenerator.generate(latitudes, longitudes, xPoints, yPoints);

        // then
        verify(routeImageUploader, times(1)).upload(any(BufferedImage.class));
    }
}
