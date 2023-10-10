package dev.tripdraw.file.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileUrlMaker {

    private final String domain;

    public FileUrlMaker(@Value("${file.common.domain}") String domain) {
        this.domain = domain;
    }

    public String make(String originalUrl, String filePath) {
        int filePathIndex = originalUrl.indexOf(filePath);
        return domain + originalUrl.substring(filePathIndex - 1);
    }
}
