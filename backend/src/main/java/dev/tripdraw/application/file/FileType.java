package dev.tripdraw.application.file;

public enum FileType {
    POST_IMAGE("jpg", "/"),
    ;

    private final String contentType;
    private final String dirPath;

    FileType(String contentType, String dirPath) {
        this.contentType = contentType;
        this.dirPath = dirPath;
    }

    public String contentType() {
        return contentType;
    }

    public String dirPath() {
        return dirPath;
    }
}
