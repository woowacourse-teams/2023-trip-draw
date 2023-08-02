package dev.tripdraw.application.file;

import static dev.tripdraw.exception.file.FileIOExceptionType.FILE_SAVE_FAIL;

import dev.tripdraw.exception.file.FileIOException;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploader {

    private final FilePath filePath;
    private final FileUrlMaker fileUrlMaker;

    public FileUploader(FilePath filePath, FileUrlMaker fileUrlMaker) {
        this.filePath = filePath;
        this.fileUrlMaker = fileUrlMaker;
    }

    public String upload(MultipartFile file, FileType fileType) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UUID id = UUID.randomUUID();
        String filePathWithBase = filePath.getPathWithBase(fileType) + id + fileType.extension();
        fileUpload(file, filePathWithBase);

        return fileUrlMaker.make(filePath.getPath(fileType) + id + fileType.extension());
    }

    private void fileUpload(MultipartFile file, String filePathWithBase) {
        try {
            file.transferTo(new File(filePathWithBase));
        } catch (IOException e) {
            throw new FileIOException(FILE_SAVE_FAIL);
        }
    }
}
