package dev.tripdraw.application.file;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import dev.tripdraw.exception.file.FileIOException;
import dev.tripdraw.exception.file.FileIOExceptionType;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FileUploaderTest {

    private final FilePath filePath = new FilePath("post-image-path");
    private final FileUrlMaker fileUrlMaker = new FileUrlMaker("ip");

    @Test
    void 파일을_업로드하고_파일의_URL을_반환한다() {
        // given
        FileUploader fileUploader = new FileUploader(filePath, fileUrlMaker);
        MockMultipartFile file = new MockMultipartFile("통후추셀카.jpg", "image의 binary".getBytes());

        // when
        String url = fileUploader.upload(file, FileType.POST_IMAGE);

        // then
        assertSoftly(softly -> {
            softly.assertThat(url).startsWith("ip" + filePath.getPath(FileType.POST_IMAGE));
            softly.assertThat(url).endsWith(FileType.POST_IMAGE.extension());
        });
    }

    @Test
    void 파일_저장에_실패할시_예외륿_발생시킨다() throws IOException {
        // given
        FileUploader fileUploader = new FileUploader(filePath, fileUrlMaker);
        MultipartFile file = mock();
        doThrow(new IOException()).when(file).transferTo((File) any());

        // expect
        assertThatThrownBy(() -> fileUploader.upload(file, FileType.POST_IMAGE))
                .isInstanceOf(FileIOException.class)
                .hasMessage(FileIOExceptionType.FILE_SAVE_FAIL.getMessage());
    }
}
