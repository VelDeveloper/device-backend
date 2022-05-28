package com.smart.device.devicebackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.smart.device.devicebackend.BaseTest;
import com.smart.device.devicebackend.RecordFactory;
import com.smart.device.devicebackend.exception.ResourceNotFound;
import com.smart.device.devicebackend.model.Device;
import com.smart.device.devicebackend.service.DeviceService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.spec.internal.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DeviceController.class)
public class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DeviceService deviceService;
    @MockBean
    private Counter counter;
    @MockBean
    private MeterRegistry meterRegistry;
    @Autowired
    private ObjectMapper objectMapper;
    private EasyRandom generator;

    @BeforeEach
    public void setup() {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .objectFactory(new RecordFactory());
        generator = new EasyRandom(parameters);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    //*************Unit test for Get all devices
    @Test
    @DisplayName("Get all devices")
    public void shouldReturnAllDevices() throws Exception {
        List<Device> devices = generator.objects(Device.class, 1)
                .collect(Collectors.toList());
        given(deviceService.getAllDevices()).willReturn(devices);
        mockMvc.perform(get("/api/v1/device/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("Get all devices should return empty for no devices in DB")
    public void shouldReturnEmptyAllDevices() throws Exception {
        given(deviceService.getAllDevices()).willReturn(new ArrayList<>());
        mockMvc.perform(get("/api/v1/device/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    //*************Unit test for Get device by id
    @Test
    @DisplayName("Get device based on id")
    public void shouldReturnItemForDeviceById() throws Exception {
        Device device = generator.nextObject(Device.class);
        given(deviceService.findDeviceById("1")).willReturn(device);
        mockMvc.perform(get("/api/v1/device/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.APPLICATION_JSON))
                .andExpect(jsonPath("deviceUid").value(device.deviceUid()))
                .andExpect(jsonPath("name").value(device.name()))
                .andExpect(jsonPath("productId").value(device.productId()))
                .andExpect(jsonPath("customerId").value(device.customerId()))
                .andExpect(jsonPath("hardwareUid").value(device.hardwareUid()));
    }

    @Test
    @DisplayName("Get device based on id should throw entity not found exception")
    public void shouldReturnExceptionForDeviceById() throws Exception {
        given(deviceService.findDeviceById("1")).willThrow(ResourceNotFound.class);
        mockMvc.perform(get("/api/v1/device/1"))
//                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    //*************Unit test for delete device
    @Test
    @DisplayName("delete device based on id")
    public void shouldSuccessForDeviceById() throws Exception {
        Device device = generator.nextObject(Device.class);
        doNothing().when(deviceService).deleteDevice("1");
        mockMvc.perform(delete("/api/v1/device/1"))
                .andExpect(status().isOk());
    }

    //*************Unit test for create device
    @Test
    @DisplayName("Create device should success")
    public void shouldSuccessForCreateDevice() throws Exception {
        Device device = generator.nextObject(Device.class);
        given(deviceService.createDevice(device)).willReturn(device);
        mockMvc.perform(post("/api/v1/device")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(device)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(device)));
    }

    //*************Unit test for update device
    @Test
    @DisplayName("Update device based on id")
    public void shouldSuccessForUpdateDevice() throws Exception {
        Device device = generator.nextObject(Device.class);
        given(deviceService.updateDevice(device)).willReturn(device);
        mockMvc.perform(put("/api/v1/device/"+device.id())
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(device)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(device)));
    }

    @Test
    @DisplayName("update device based on id should return entity not found")
    public void shouldFailForUpdateDevice() throws Exception {
        Device device = generator.nextObject(Device.class);
        given(deviceService.updateDevice(device)).willThrow(ResourceNotFound.class);
        mockMvc.perform(put("/api/v1/device/"+device.id())
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(device)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
