package com.smart.device.devicebackend.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record SQS(String deviceOrderQueue) {
}
