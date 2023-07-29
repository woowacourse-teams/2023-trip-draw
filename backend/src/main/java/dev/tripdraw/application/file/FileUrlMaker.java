package dev.tripdraw.application.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileUrlMaker {

    private final String ip;

    public FileUrlMaker(@Value("${ip}") String ip) {
        this.ip = ip;
    }

    public String make(String fullPath) {
        return ip + fullPath;
    }
}
