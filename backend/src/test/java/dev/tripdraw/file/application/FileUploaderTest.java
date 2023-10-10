package dev.tripdraw.file.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
class FileUploaderTest {

    @Mock
    private AmazonS3 amazonS3;

    @Mock
    private MultipartFile multipartFile;

    private FileUrlMaker fileUrlMaker;
    private String bucket;

    @BeforeEach
    void setUp() {
        String domain = "https://domain.com";
        fileUrlMaker = new FileUrlMaker(domain);
        bucket = "test-bucket";
    }

    @Test
    void 파일을_업로드한다() throws IOException {
        // given
        String path = "root/directory/image.jpg";
        given(multipartFile.getInputStream()).willReturn(new ByteArrayInputStream("file".getBytes()));
        given(amazonS3.getUrl(bucket, path)).willReturn(new URL("https://original.com/root/directory/image.jpg"));

        FileUploader fileUploader = new FileUploader(amazonS3, fileUrlMaker, bucket);

        // when
        fileUploader.upload(path, multipartFile);

        // then
        then(amazonS3)
                .should(times(1))
                .putObject(anyString(), anyString(), any(InputStream.class), any(ObjectMetadata.class));
    }

    @Test
    void 파일을_업로드하면_URL을_반환한다() throws IOException {
        // given
        String path = "root/directory/image.jpg";
        given(amazonS3.getUrl(bucket, path)).willReturn(new URL("https://original.com/root/directory/image.jpg"));

        FileUploader fileUploader = new FileUploader(amazonS3, fileUrlMaker, bucket);

        // when
        String url = fileUploader.upload(path, multipartFile);

        // then
        assertThat(url).isEqualTo("https://domain.com/root/directory/image.jpg");
    }
}
