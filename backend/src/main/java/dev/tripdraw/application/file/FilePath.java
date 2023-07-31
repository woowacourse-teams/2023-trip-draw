package dev.tripdraw.application.file;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FilePath {

    private final String postImagePath;

    public FilePath(@Value("${path.post-image}") String postImagePath) {
        this.postImagePath = postImagePath;
    }

    public String getPath(FileType fileType) {
        Map<FileType, String> typeAndPath = Map.of(
                FileType.POST_IMAGE, postImagePath
        );

        return typeAndPath.get(fileType);
    }
}
