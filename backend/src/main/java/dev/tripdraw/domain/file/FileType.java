package dev.tripdraw.domain.file;

import static dev.tripdraw.file.exception.FileIOExceptionType.INVALID_CONTENT_TYPE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import dev.tripdraw.file.exception.FileIOException;
import java.util.Arrays;
import java.util.Objects;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public enum FileType {
    POST_IMAGE(IMAGE_JPEG_VALUE, ".jpg"),
    ;

    private final String contentType;
    private final String extension;

    FileType(String contentType, String extension) {
        this.contentType = contentType;
        this.extension = extension;
    }

    public static FileType from(String contentType) {
        return Arrays.stream(FileType.values())
                .filter(fileType -> Objects.equals(fileType.contentType(), contentType))
                .findFirst()
                .orElseThrow(() -> new FileIOException(INVALID_CONTENT_TYPE));
    }
}
