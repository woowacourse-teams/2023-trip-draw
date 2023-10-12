package dev.tripdraw.post.application;

import static dev.tripdraw.file.domain.FileType.IMAGE_JPEG;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PostFilePathTest {

    @Test
    void 파일_유형에_따라_감상_파일의_경로를_얻는다() {
        // given
        String root = "root/";
        String directory = "directory/";
        PostFilePath postFilePath = new PostFilePath(root, directory);

        // when
        String path = postFilePath.getPath(IMAGE_JPEG);

        // then
        assertThat(path).isEqualTo("root/directory/");
    }
}
