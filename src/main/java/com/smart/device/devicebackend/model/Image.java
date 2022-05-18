package com.smart.device.devicebackend.model;

import org.springframework.data.annotation.Id;

public record Image(@Id String imageId, String imageName, String description, String filePath) {
}
