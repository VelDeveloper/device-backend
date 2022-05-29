package com.smart.device.devicebackend.service;

import com.smart.device.devicebackend.model.DeviceOrderEvent;
import com.smart.device.devicebackend.repository.DeviceOrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class DeviceOrderEventMessageListener {
    private DeviceOrderRepository deviceOrderRepository;
    @SqsListener(value = "${aws.sqs.device-order-queue}")
    public void processMessage(@Payload DeviceOrderEvent orderEvent) {
        log.info("Incoming order: '{}'", orderEvent);
        deviceOrderRepository.save(orderEvent);
        log.info("Successfully processed event");
    }
}
