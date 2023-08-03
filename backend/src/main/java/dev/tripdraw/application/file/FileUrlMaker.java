package dev.tripdraw.application.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileUrlMaker {

    @Value("${trip.domain}")
    private String domain;

    public String make(String filePath) {
        return domain + filePath;
    }
}
