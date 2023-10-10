package dev.tripdraw.file.application;

import static dev.tripdraw.file.domain.FileType.IMAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

import dev.tripdraw.file.exception.FileIOException;
import dev.tripdraw.post.application.PostFileManager;
import dev.tripdraw.post.application.PostFilePath;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
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
class PostFileManagerTest {

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private FileUploader fileUploader;

    private PostFilePath postFilePath;

    @BeforeEach
    void setUp() {
        String root = "root/";
        String directory = "directory/";
        postFilePath = new PostFilePath(root, directory);
    }

    @Test
    void 파일을_업로드하면_URL을_반환한다() throws IOException {
        // given
        given(multipartFile.getContentType()).willReturn(IMAGE.contentType());
        given(fileUploader.upload(anyString(), any(MultipartFile.class)))
                .willReturn("https://domain.com/root/directory/fileName");

        PostFileManager postFileManager = new PostFileManager(postFilePath, fileUploader);

        // when
        String url = postFileManager.upload(multipartFile);

        // then
        assertThat(url).isEqualTo("https://domain.com/root/directory/fileName");
    }

    @Test
    void 파일을_업로드한다() throws IOException {
        // given
        given(multipartFile.getContentType()).willReturn(IMAGE.contentType());

        PostFileManager postFileManager = new PostFileManager(postFilePath, fileUploader);

        // when
        postFileManager.upload(multipartFile);

        // then
        then(fileUploader)
                .should(times(1))
                .upload(anyString(), any(MultipartFile.class));
    }

    @Test
    void 파일을_업로드할_때_실패하면_예외를_발생시킨다() throws IOException {
        // given
        given(multipartFile.getContentType()).willReturn(IMAGE.contentType());
        given(fileUploader.upload(anyString(), any(MultipartFile.class))).willThrow(new IOException());

        PostFileManager postFileManager = new PostFileManager(postFilePath, fileUploader);

        // expect
        assertThatThrownBy(() -> postFileManager.upload(multipartFile))
                .isInstanceOf(FileIOException.class)
                .hasMessage("파일 저장에 실패했습니다.");
    }
}
