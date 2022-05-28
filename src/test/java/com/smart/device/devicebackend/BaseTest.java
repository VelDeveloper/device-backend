package com.smart.device.devicebackend;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.*;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
@ActiveProfiles("test")
public class BaseTest {
    @Container
    static LocalStackContainer localStack =
            new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
                    .withServices(S3, SQS)
                    .withEnv("LOCALSTACK_HOSTNAME", "localhost")
                    .withEnv("HOSTNAME", "localhost");

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.3");

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", "device-order-queue");
        localStack.execInContainer("awslocal", "s3", "mb", "s3://" + "image-bucket");
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void overrideConfiguration(DynamicPropertyRegistry registry) {
        registry.add("aws.sqs.device-order-queue", () -> "device-order-queue");
        registry.add("aws.s3.bucket", () -> "image-bucket");
        registry.add("aws.region", () -> localStack.getRegion());
        registry.add("cloud.aws.sqs.endpoint", () -> localStack.getEndpointConfiguration(SQS).getServiceEndpoint());
        registry.add("cloud.aws.s3.endpoint", () -> localStack.getEndpointConfiguration(S3).getServiceEndpoint());
        registry.add("cloud.aws.s3.region", () -> localStack.getRegion());
        registry.add("cloud.aws.credentials.access-key", localStack::getAccessKey);
        registry.add("cloud.aws.credentials.secret-key", localStack::getSecretKey);
        registry.add("aws.access-key", localStack::getAccessKey);
        registry.add("aws.secret-key", localStack::getSecretKey);
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("cloud.aws.stack.auto", () -> false);
        registry.add("cloud.aws.stack.enabled", () -> false);
        registry.add("cloud.aws.region.static", () -> "eu-central-1");
        registry.add("spring.profiles.active", () -> "test");
    }

    @TestConfiguration
    @ActiveProfiles("test")
    static class AwsTestConfig {

//        @Bean
//        public AmazonS3 amazonS3() {
//            return AmazonS3ClientBuilder.standard()
//                    .withCredentials(localStack.getDefaultCredentialsProvider())
//                    .withEndpointConfiguration(localStack.getEndpointConfiguration(S3))
//                    .build();
//        }
        @Bean
        @Primary
        public S3Client s3Client() {
            return  S3Client.builder()
                    .region(Region.of(localStack.getRegion()))
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())))
                    .build();
        }

        @Bean
        @Primary
        public AmazonSQSAsync amazonSQSAsync() {
            return AmazonSQSAsyncClientBuilder.standard()
                    .withCredentials(localStack.getDefaultCredentialsProvider())
                    .withEndpointConfiguration(localStack.getEndpointConfiguration(SQS))
                    .build();
        }

        @Bean
        @Primary
        public QueueMessagingTemplate queueMessagingTemplate() {
            return new QueueMessagingTemplate(amazonSQSAsync());
        }
    }
}
