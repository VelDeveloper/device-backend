package com.smart.device.devicebackend.controller;

import com.smart.device.devicebackend.configuration.AWSConfigProperties;
import com.smart.device.devicebackend.model.DeviceOrderEvent;
import com.smart.device.devicebackend.service.AwsSQSProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/sqs")
@AllArgsConstructor
@Slf4j
public class AwsSQSController {

    private QueueMessagingTemplate queueMessagingTemplate;
    private AWSConfigProperties awsConfigProperties;

    @PostMapping(value = "/publish/device/order/event", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void publishDeviceOrderEvent(@RequestBody DeviceOrderEvent deviceOrderEvent) {
        log.debug("publish order event {}", deviceOrderEvent);
        queueMessagingTemplate.convertAndSend(awsConfigProperties.sqs().deviceOrderQueue(), deviceOrderEvent);
    }
}
