package com.smart.device.devicebackend.configuration;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@AllArgsConstructor
@Profile("!test")
public class S3Config {

    private AWSConfigProperties awsConfigProperties;

    @Bean(destroyMethod = "close")
    public S3Client s3Client() throws URISyntaxException {
        S3ClientBuilder s3ClientBuilder = S3Client.builder()
                .region(Region.of(awsConfigProperties.region()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsConfigProperties.accessKey(), awsConfigProperties.secretKey())));
        if (StringUtils.isNotBlank(awsConfigProperties.endpoint())) {
            s3ClientBuilder.endpointOverride(new URI(awsConfigProperties.endpoint()));
        }
        return s3ClientBuilder.build();
    }

}
