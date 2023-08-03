package dev.tripdraw.application.file;

import static dev.tripdraw.domain.file.FileType.POST_IMAGE;

import dev.tripdraw.domain.file.FileType;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FilePath {

    @Value("${trip.base}")
    private String base;

    @Value("${trip.post}")
    private String postImagePath;

    public String getPath(FileType fileType) {
        Map<FileType, String> typeAndPath = Map.of(
                POST_IMAGE, postImagePath
        );

        return typeAndPath.get(fileType);
    }

    public String getPathWithBase(FileType fileType) {
        Map<FileType, String> typeAndPath = Map.of(
                POST_IMAGE, base + postImagePath
        );

        return typeAndPath.get(fileType);
    }
}
