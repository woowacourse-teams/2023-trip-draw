package dev.tripdraw.draw.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.draw.application.ImageMultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ImageMultipartFileTest {

    @Test
    void 이름을_반환한다() {
        // given
        MultipartFile file = ImageMultipartFile.of(new ByteArrayOutputStream(), "herb.jpg");

        // when
        String name = file.getName();

        // then
        assertThat(name).isEqualTo("herb");
    }

    @Test
    void 파일_전체_이름을_반환한다() {
        // given
        MultipartFile file = ImageMultipartFile.of(new ByteArrayOutputStream(), "herb.jpg");

        // when
        String originalFilename = file.getOriginalFilename();

        // then
        assertThat(originalFilename).isEqualTo("herb.jpg");
    }

    @Test
    void 확장자를_반환한다() {
        // given
        MultipartFile file = ImageMultipartFile.of(new ByteArrayOutputStream(), "herb.jpg");

        // when
        String contentType = file.getContentType();

        // then
        assertThat(contentType).isEqualTo("jpg");
    }

    @Test
    void 빈_스트림을_입력한_후_비었는지_확인한다() {
        // given
        MultipartFile file = ImageMultipartFile.of(new ByteArrayOutputStream(), "herb.jpg");

        // when
        boolean actual = file.isEmpty();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 찬_스트림을_입력한_후_비었는지_확인한다() throws IOException {
        // given
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("herb".getBytes());

        MultipartFile file = ImageMultipartFile.of(byteArrayOutputStream, "herb.jpg");

        // when
        boolean actual = file.isEmpty();

        // then
        assertThat(actual).isFalse();
    }


    @Test
    void 사이즈를_반환한다() throws IOException {
        // given
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("herb".getBytes());

        MultipartFile file = ImageMultipartFile.of(byteArrayOutputStream, "herb.jpg");

        // when
        long size = file.getSize();

        // then
        assertThat(size).isEqualTo("herb".getBytes().length);
    }

    @Test
    void 바이트_배열을_반환한다() throws IOException {
        // given
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("herb".getBytes());

        MultipartFile file = ImageMultipartFile.of(byteArrayOutputStream, "herb.jpg");

        // when
        byte[] bytes = file.getBytes();

        // then
        assertThat(bytes).isEqualTo("herb".getBytes());
    }

    @Test
    void InputStream을_반환한다() throws IOException {
        // given
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("herb".getBytes());

        MultipartFile file = ImageMultipartFile.of(byteArrayOutputStream, "herb.jpg");

        // when
        InputStream inputStream = file.getInputStream();

        // then
        assertThat(inputStream.readAllBytes()).isEqualTo("herb".getBytes());
    }

    @Test
    void 파일을_이동할_경우_예외를_던진다() throws IOException {
        // given
        MultipartFile file = ImageMultipartFile.of(new ByteArrayOutputStream(), "herb.jpg");
        File destination = new File("/image");

        // expect
        assertThatThrownBy(() -> file.transferTo(destination))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
