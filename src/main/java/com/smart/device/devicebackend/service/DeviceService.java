package com.smart.device.devicebackend.service;

import com.smart.device.devicebackend.exception.ResourceNotFound;
import com.smart.device.devicebackend.model.Device;
import com.smart.device.devicebackend.repository.DeviceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class DeviceService {
    private DeviceRepository deviceRepository;

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Device findDeviceById(String id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(id));
    }

    public Device findDeviceByDeviceUid(String deviceUid) {
        return deviceRepository.findByDeviceUid(deviceUid)
                .orElseThrow(() -> new ResourceNotFound(deviceUid));
    }

    public Device createDevice(Device device) {
        return deviceRepository.findByDeviceUid(device.deviceUid())
                .orElseGet(() -> deviceRepository.save(device));
    }

    public Device updateDevice(Device device) {
        return deviceRepository.findById(device.id())
                .map(deviceRepository::save)
                .orElseThrow(() -> new ResourceNotFound(device.id()));
    }

    public void deleteDevice(String id) {
        deviceRepository.deleteById(id);
    }
}
