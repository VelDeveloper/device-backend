package com.smart.device.devicebackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record Device(@Id String id, String deviceUid, String name, String hardwareUid, String productId, boolean deleted, String customerId) {
}
