package com.smart.device.devicebackend.controller;

import com.smart.device.devicebackend.model.Image;
import com.smart.device.devicebackend.service.AwsS3Service;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController("/api/v1/s3")
@AllArgsConstructor
public class AwsS3Controller {

    private AwsS3Service awsS3Service;

    @PostMapping("/upload")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Image> createDevice(@RequestParam String imageName,
                                              @RequestParam String description,
                                              @RequestParam MultipartFile file) {
        Image upload = awsS3Service.upload(imageName, description, file);
        return ResponseEntity.ok(upload);
    }

    @GetMapping("/image/download/{id}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<byte[]> createDevice(@PathVariable String id) {
        return ResponseEntity.ok(awsS3Service.download(id));
    }
}
