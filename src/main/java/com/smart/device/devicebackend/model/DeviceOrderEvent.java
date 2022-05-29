package com.smart.device.devicebackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record DeviceOrderEvent(@Id String id, String product, String deviceUid, String message, boolean expressDelivery) {
}
