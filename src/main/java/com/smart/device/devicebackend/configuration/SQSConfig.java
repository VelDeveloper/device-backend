package com.smart.device.devicebackend.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static com.smart.device.devicebackend.DeviceBackendConstants.ENDPOINT;
import static com.smart.device.devicebackend.DeviceBackendConstants.EU_CENTRAL_1;

@Configuration
@AllArgsConstructor
@Profile("!test")
public class SQSConfig {

    private AWSConfigProperties awsConfigProperties;
    @Bean(destroyMethod = "shutdown")
    @Primary
    public AmazonSQSAsync amazonSQSAsync() {
        AmazonSQSAsyncClientBuilder amazonSQSAsyncClientBuilder = AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsConfigProperties.accessKey(), awsConfigProperties.secretKey())));
        if (StringUtils.isNotBlank(awsConfigProperties.endpoint())) {
            amazonSQSAsyncClientBuilder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(ENDPOINT, EU_CENTRAL_1));
        } else {
            amazonSQSAsyncClientBuilder.withRegion(awsConfigProperties.region());
        }
        return amazonSQSAsyncClientBuilder.build();
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(final AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

}
