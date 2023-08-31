package dev.tripdraw.file.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class FileUrlMakerTest {

    private final FileUrlMaker fileUrlMaker = new FileUrlMaker("");

    @Test
    void 이미지의_URL을_생성한다() {
        // given
        String fullPath = "/통후추/통후추의셀카.jpg";

        // when
        String url = fileUrlMaker.make(fullPath);

        // then
        assertThat(url).isEqualTo("/통후추/통후추의셀카.jpg");
    }
}
