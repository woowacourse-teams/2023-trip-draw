package dev.tripdraw.application.file;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FileUrlMakerTest {

    private final FileUrlMaker fileUrlMaker = new FileUrlMaker("ip");

    @Test
    void 이미지의_URL을_생성한다() {
        // given
        String fullPath = "/통후추/통후추의셀카.jpg";

        // when
        String url = fileUrlMaker.make(fullPath);

        // then
        Assertions.assertThat(url).isEqualTo("ip" + "/통후추/통후추의셀카.jpg");
    }
}
