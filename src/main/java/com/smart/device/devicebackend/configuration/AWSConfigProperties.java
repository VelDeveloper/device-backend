package com.smart.device.devicebackend.configuration;

import com.smart.device.devicebackend.model.S3;
import com.smart.device.devicebackend.model.SQS;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "aws")
@ConstructorBinding
public record AWSConfigProperties(
        String accessKey,
        String secretKey,
        String region,
        S3 s3,
        SQS sqs
) {
}
