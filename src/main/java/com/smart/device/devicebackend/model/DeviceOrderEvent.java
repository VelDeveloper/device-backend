package com.smart.device.devicebackend.model;

public record DeviceOrderEvent(String id, String product, String deviceUid, String message, boolean expressDelivery) {
}
