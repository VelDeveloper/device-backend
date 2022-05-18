package com.smart.device.devicebackend.configuration;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import lombok.AllArgsConstructor;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class SQSConfig {

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQS) {
        return new QueueMessagingTemplate(amazonSQS);
    }

}
