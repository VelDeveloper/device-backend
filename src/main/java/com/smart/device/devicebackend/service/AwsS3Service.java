package com.smart.device.devicebackend.service;

import com.smart.device.devicebackend.exception.ServiceException;
import com.smart.device.devicebackend.model.Image;
import com.smart.device.devicebackend.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Slf4j
public class AwsS3Service {

    private S3Client s3Client;
    private ImageRepository imageRepository;

    public Image upload(String imageName, String description, MultipartFile file) {
        try {
            log.info("Uploading a file to s3. ImageName: {}, Description: {}", imageName, description);
            if (file.isEmpty()) {
                throw new ServiceException("Cannot upload empty file");
            }
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .bucket("dev-device-backend")
                    .key(file.getOriginalFilename())
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            Image image = new Image(ObjectId.get().toHexString(), imageName, description, file.getOriginalFilename());
            return imageRepository.save(image);
        } catch (SdkServiceException sse) {
            log.error("Caught an AmazonServiceException, which " + "means your request made it "
                    + "to Amazon S3, but was rejected with an error response" + " for some reason.", sse);
            log.info("Error Message:    " + sse.getMessage());
            log.info("Key:       " + file.getOriginalFilename());
            throw sse;
        } catch (SdkClientException ace) {
            log.error("Caught an AmazonClientException, which " + "means the client encountered "
                    + "an internal error while trying to " + "communicate with S3, "
                    + "such as not being able to access the network.", ace);
            log.error("Error Message: {}, {}", file.getOriginalFilename(), ace.getMessage());
            throw ace;
        } catch (Exception e) {
            log.error("Internal server error encountered", e);
            throw new ServiceException(e.getMessage());
        }
    }

    public byte[] download(String keyName) {
        try {
            log.info("Retrieving file from S3 for key: {}", keyName);
            ResponseBytes<GetObjectResponse> s3Object = s3Client.getObject(
                    GetObjectRequest.builder().bucket("dev-device-backend").key(keyName).build(),
                    ResponseTransformer.toBytes());
            return s3Object.asByteArray();
        } catch (SdkClientException ase) {
            log.error("Caught an AmazonServiceException, which " + "means your request made it "
                    + "to Amazon S3, but was rejected with an error response" + " for some reason: " + keyName, ase);
            throw ase;
        } catch (SdkServiceException ace) {
            log.error("Caught an AmazonClientException, which " + "means the client encountered "
                    + "an internal error while trying to " + "communicate with S3, "
                    + "such as not being able to access the network: " + keyName, ace);
            throw ace;
        }
    }
}
