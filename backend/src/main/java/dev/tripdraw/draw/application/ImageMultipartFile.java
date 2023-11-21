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

    private ImageMultipartFile(ByteArrayOutputStream byteArrayOutputStream, String originalFilename) {
        this.byteArrayOutputStream = byteArrayOutputStream;
        this.originalFilename = originalFilename;
    }

    public static ImageMultipartFile of(ByteArrayOutputStream byteArrayOutputStream, String originalFilename) {
        return new ImageMultipartFile(byteArrayOutputStream, originalFilename);
    }

    @Override
    public String getName() {
        int indexOfDelimiter = originalFilename.indexOf(DELIMITER);
        return originalFilename.substring(BEGIN_INDEX, indexOfDelimiter);
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        int indexOfDelimiter = originalFilename.indexOf(DELIMITER);
        return originalFilename.substring(indexOfDelimiter + LENGTH_OF_DELIMITER);
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
