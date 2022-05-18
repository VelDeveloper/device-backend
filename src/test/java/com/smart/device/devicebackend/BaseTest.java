package com.smart.device.devicebackend;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.*;

@Testcontainers
@SpringBootTest
public class BaseTest {
    @Container
    static LocalStackContainer localStack =
            new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.13.0"))
                    .withServices(S3, SQS);

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.3");

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", "device-order-queue");
        localStack.execInContainer("awslocal", "s3", "mb", "s3://" + "image-bucket");
    }

    @DynamicPropertySource
    static void overrideConfiguration(DynamicPropertyRegistry registry) {
        registry.add("aws.sqs.device-order-queue", () -> "device-order-queue");
        registry.add("aws.s3.bucket", () -> "image-bucket");
        registry.add("cloud.aws.sqs.endpoint", () -> localStack.getEndpointOverride(SQS));
        registry.add("cloud.aws.s3.endpoint", () -> localStack.getEndpointOverride(S3));
        registry.add("cloud.aws.credentials.access-key", localStack::getAccessKey);
        registry.add("cloud.aws.credentials.secret-key", localStack::getSecretKey);
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }
}
