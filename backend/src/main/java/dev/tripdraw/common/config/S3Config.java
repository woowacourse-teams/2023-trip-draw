package dev.tripdraw.common.config;

import static com.amazonaws.regions.Regions.AP_NORTHEAST_2;

import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(AP_NORTHEAST_2)
                .withCredentials(new EC2ContainerCredentialsProviderWrapper())
                .build();
    }
}
