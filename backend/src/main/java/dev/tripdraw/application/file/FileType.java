package dev.tripdraw.application.file;

public enum FileType {
    POST_IMAGE(".jpg"),
    ;

    private final String extension;

    FileType(String extension) {
        this.extension = extension;
    }

    public String extension() {
        return extension;
    }
}
