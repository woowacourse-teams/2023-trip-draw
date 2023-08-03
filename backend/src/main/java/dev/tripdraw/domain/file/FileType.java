package dev.tripdraw.domain.file;

import static dev.tripdraw.exception.file.FileIOExceptionType.INVALID_CONTENT_TYPE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import dev.tripdraw.exception.file.FileIOException;
import java.util.Arrays;
import java.util.Objects;

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

    public String contentType() {
        return contentType;
    }

    public String extension() {
        return extension;
    }
}
