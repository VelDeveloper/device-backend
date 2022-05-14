package com.smart.device.devicebackend.repository;

import com.smart.device.devicebackend.model.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {
    Optional<Device> findByDeviceUid(String deviceUid);
}
