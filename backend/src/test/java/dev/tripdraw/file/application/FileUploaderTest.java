package dev.tripdraw.file.application;

import static dev.tripdraw.file.domain.FileType.IMAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.tripdraw.file.exception.FileIOException;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class FileUploaderTest {

    @Mock
    private FilePath filePath;

    @Mock
    private FileUrlMaker fileUrlMaker;

    @InjectMocks
    private FileUploader fileUploader;

    @Test
    void 파일의_URL을_반환한다() {
        // given
        UUID randomUUID = UUID.randomUUID();
        String baseUrl = "https://example.com/files/";
        String expectedFileUrl = baseUrl + randomUUID + ".jpg";
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        when(multipartFile.getContentType()).thenReturn(IMAGE.contentType());
        when(fileUrlMaker.make(any())).thenReturn(expectedFileUrl);

        // when
        String url = fileUploader.upload(multipartFile);

        // then
        assertThat(url).isEqualTo(expectedFileUrl);
    }

    @Test
    void 파일을_업로드_한다() throws IOException {
        // given
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        when(multipartFile.getContentType()).thenReturn(IMAGE.contentType());

        // when
        fileUploader.upload(multipartFile);

        // then
        verify(multipartFile, times(1)).transferTo(any(File.class));
    }

    @Test
    void 파일_저장에_실패할시_예외륿_발생시킨다() throws IOException {
        // given
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        when(multipartFile.getContentType()).thenReturn(IMAGE.contentType());
        doThrow(new IOException()).when(multipartFile).transferTo(any(File.class));

        // expect
        assertThatThrownBy(() -> fileUploader.upload(multipartFile))
                .isInstanceOf(FileIOException.class);
    }
}
