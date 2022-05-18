package com.smart.device.devicebackend.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "aws")
@ConstructorBinding
public record AWSConfigProperties(
        String accessKey,
        String secretKey,
        String region
) {
}
