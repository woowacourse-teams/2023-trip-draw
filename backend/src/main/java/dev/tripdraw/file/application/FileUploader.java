package dev.tripdraw.file.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploader {

    private final AmazonS3 amazonS3;
    private final FileUrlMaker fileUrlMaker;
    private final String bucket;

    public FileUploader(AmazonS3 amazonS3, FileUrlMaker fileUrlMaker, @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.amazonS3 = amazonS3;
        this.fileUrlMaker = fileUrlMaker;
        this.bucket = bucket;
    }

    public String upload(String path, MultipartFile file) throws IOException {
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentType(file.getContentType());
        metaData.setContentLength(file.getSize());

        amazonS3.putObject(bucket, path, file.getInputStream(), metaData);

        return fileUrlMaker.make(amazonS3.getUrl(bucket, path).toString(), path);
    }
}
