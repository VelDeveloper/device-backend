package com.smart.device.devicebackend.model;

import java.time.Instant;

public record ErrorMessage(int statusCode, Instant timestamp, String message) {
}
