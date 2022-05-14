package com.smart.device.devicebackend.controller;

import com.smart.device.devicebackend.model.Device;
import com.smart.device.devicebackend.service.DeviceService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
public class DeviceController {
    private DeviceService deviceService;
    private Counter hitCounter;

    public DeviceController(DeviceService deviceService, MeterRegistry meterRegistry) {
        hitCounter = Counter.builder("hit_counter")
                .description("Number of hits")
                .register(meterRegistry);
        this.deviceService = deviceService;
    }

    @PostMapping("/device")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Device createDevice(@RequestBody Device device) {
        return deviceService.createDevice(device);
    }

    @PutMapping("/device/{deviceUid}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public Device updateDevice(@PathVariable String deviceUid, @RequestBody Device device) {
        return deviceService.updateDevice(device);
    }

    @GetMapping("/device/all")
    public List<Device> getAllDevice() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/device/{id}")
    public Device findDeviceById(@PathVariable String id){
//        hitCounter.increment();
        return deviceService.findDeviceById(id);
    }

    @DeleteMapping("/device/{id}")
    public void deleteDevice(@PathVariable String id) {
        deviceService.deleteDevice(id);
    }

}
