package com.smart.device.devicebackend.controller;

import com.smart.device.devicebackend.model.Image;
import com.smart.device.devicebackend.service.AwsS3Service;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping(value = "/api/v1/s3")
@AllArgsConstructor
public class AwsS3Controller {

    private AwsS3Service awsS3Service;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Image> createDevice(@RequestParam String imageName,
                                              @RequestParam String imageId,
                                              @RequestParam String description,
                                              @RequestParam MultipartFile file) {
        Image upload = awsS3Service.upload(imageName, imageId, description, file);
        return ResponseEntity.ok(upload);
    }

    @GetMapping(value = "/image/download/{id}", produces = {IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<byte[]> createDevice(@PathVariable String id) {
        return ResponseEntity.ok(awsS3Service.download(id));
    }
}
