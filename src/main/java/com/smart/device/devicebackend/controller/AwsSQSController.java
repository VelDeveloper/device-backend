package com.smart.device.devicebackend.controller;

import com.smart.device.devicebackend.model.DeviceOrderEvent;
import com.smart.device.devicebackend.service.AwsSQSProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/sqs")
@AllArgsConstructor
@Slf4j
public class AwsSQSController {

    private AwsSQSProducer awsSQSProducer;

    @PostMapping("/publish/device/order/event")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void publishDeviceOrderEvent(@RequestBody DeviceOrderEvent deviceOrderEvent) {
        log.debug("publish order event {}", deviceOrderEvent);
        awsSQSProducer.publishDeviceOrderEvent(deviceOrderEvent);
    }
}
