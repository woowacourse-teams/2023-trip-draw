package dev.tripdraw.file.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileUrlMaker {

    private final String domain;

    public FileUrlMaker(@Value("${trip.domain}") String domain) {
        this.domain = domain;
    }

    public String make(String filePath) {
        return domain + filePath;
    }
}
