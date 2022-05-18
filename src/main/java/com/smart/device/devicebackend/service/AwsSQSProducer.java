package com.smart.device.devicebackend.service;

import com.smart.device.devicebackend.configuration.AWSConfigProperties;
import com.smart.device.devicebackend.model.DeviceOrderEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class AwsSQSProducer {

    private QueueMessagingTemplate queueMessagingTemplate;
    private AWSConfigProperties awsConfigProperties;

    public void publishDeviceOrderEvent(DeviceOrderEvent deviceOrderEvent) {
        queueMessagingTemplate.convertAndSend(awsConfigProperties.sqs().deviceOrderQueue(), deviceOrderEvent);
        log.info("publish device order event {}", deviceOrderEvent);
    }
}
