package com.smart.device.devicebackend.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@AllArgsConstructor
public class S3Config {

    private AWSConfigProperties awsConfigProperties;

    @Bean(destroyMethod = "close")
    public S3Client s3Client() {
        return  S3Client.builder()
                .region(Region.of(awsConfigProperties.region()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsConfigProperties.accessKey(), awsConfigProperties.secretKey())))
                .build();
    }
}
