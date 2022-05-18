package com.smart.device.devicebackend.service;

import com.smart.device.devicebackend.model.DeviceOrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeviceOrderEventMessageListener {

    @SqsListener(value = "${aws.sqs.device-order-queue}")
    public void processMessage(@Payload DeviceOrderEvent orderEvent) {
        log.info("Incoming order: '{}'", orderEvent);

        log.info("Successfully processed event");
    }
}
