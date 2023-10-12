package dev.tripdraw.post.application;

import static dev.tripdraw.file.exception.FileIOExceptionType.FILE_SAVE_FAIL;

import dev.tripdraw.file.application.FileUploader;
import dev.tripdraw.file.domain.FileType;
import dev.tripdraw.file.exception.FileIOException;
import java.io.IOException;
import java.util.UUID;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Component
public class PostFileUploader {

    private static final String DOT = ".";

    private final PostFilePath postFilePath;
    private final FileUploader fileUploader;

    public PostFileUploader(PostFilePath postFilePath, FileUploader fileUploader) {
        this.postFilePath = postFilePath;
        this.fileUploader = fileUploader;
    }

    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        FileType type = FileType.from(file.getContentType());
        String fileName = UUID.randomUUID() + DOT + type.extension();
        String path = postFilePath.getPath(type) + fileName;

        return fileUpload(path, file);
    }

    private String fileUpload(String path, MultipartFile file) {
        try {
            return fileUploader.upload(path, file);
        } catch (IOException e) {
            throw new FileIOException(FILE_SAVE_FAIL);
        }
    }
}
