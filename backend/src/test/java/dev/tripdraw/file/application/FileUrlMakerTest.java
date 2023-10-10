package dev.tripdraw.file.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class FileUrlMakerTest {

    private FileUrlMaker fileUrlMaker;

    @BeforeEach
    void setUp() {
        fileUrlMaker = new FileUrlMaker("https://domain.com");
    }

    @Test
    void 이미지의_URL을_생성한다() {
        // given
        String filePath = "root/directory/image.jpg";
        String originalUrl = "https://original.com/root/directory/image.jpg";

        // when
        String url = fileUrlMaker.make(originalUrl, filePath);

        // then
        assertThat(url).isEqualTo("https://domain.com/root/directory/image.jpg");
    }
}
