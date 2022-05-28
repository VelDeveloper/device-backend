package com.smart.device.devicebackend.configuration;

import com.smart.device.devicebackend.model.S3;
import com.smart.device.devicebackend.model.SQS;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@ConfigurationProperties(prefix = "aws")
@ConstructorBinding
@Validated
public record AWSConfigProperties(
        @NotEmpty
        String accessKey,
        @NotEmpty
        String secretKey,
        String region,
        String endpoint,
        S3 s3,
        SQS sqs
) {
}
