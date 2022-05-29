package com.smart.device.devicebackend.repository;

import com.smart.device.devicebackend.model.DeviceOrderEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceOrderRepository extends MongoRepository<DeviceOrderEvent, String> {
}
