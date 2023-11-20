package dev.tripdraw.draw.application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public class ImageMultipartFile implements MultipartFile {

    private static final String DELIMITER = ".";
    private static final int BEGIN_INDEX = 0;
    private static final int LENGTH_OF_DELIMITER = 1;

    private final ByteArrayOutputStream byteArrayOutputStream;
    private final String originalFilename;
    private final String name;
    private final String contentType;

    private ImageMultipartFile(
            ByteArrayOutputStream byteArrayOutputStream,
            String originalFilename,
            String name,
            String contentType
    ) {
        this.byteArrayOutputStream = byteArrayOutputStream;
        this.originalFilename = originalFilename;
        this.name = name;
        this.contentType = contentType;
    }

    public static ImageMultipartFile of(ByteArrayOutputStream byteArrayOutputStream, String originalFilename) {
        int indexOfDelimiter = originalFilename.indexOf(DELIMITER);
        String name = originalFilename.substring(BEGIN_INDEX, indexOfDelimiter);
        String contentType = originalFilename.substring(indexOfDelimiter + LENGTH_OF_DELIMITER);
        return new ImageMultipartFile(byteArrayOutputStream, originalFilename, name, contentType);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return byteArrayOutputStream.size() == 0;
    }

    @Override
    public long getSize() {
        return byteArrayOutputStream.size();
    }

    @Override
    public byte[] getBytes() {
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException();
    }
}
