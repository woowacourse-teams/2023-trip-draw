package dev.tripdraw.post.application;

import static dev.tripdraw.file.domain.FileType.IMAGE;

import dev.tripdraw.file.domain.FileType;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PostFilePath {

    private final Map<FileType, String> pathByTypes;

    public PostFilePath(@Value("${file.common.root}") String root, @Value("${file.post.directory}") String directory) {
        this.pathByTypes = Map.of(IMAGE, root + directory);
    }

    public String getPath(FileType fileType) {
        return pathByTypes.get(fileType);
    }
}
