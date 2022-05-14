package com.smart.device.devicebackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.smart.device.devicebackend.RecordFactory;
import com.smart.device.devicebackend.exception.ResourceNotFound;
import com.smart.device.devicebackend.model.Device;
import com.smart.device.devicebackend.repository.DeviceRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {

    @InjectMocks
    private DeviceService sut;
    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    private ObjectMapper objectMapper;
    private EasyRandom generator;

    @BeforeEach
    public void setup(){
        EasyRandomParameters parameters = new EasyRandomParameters()
                .objectFactory(new RecordFactory());
        generator = new EasyRandom(parameters);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Test
    @DisplayName("Get all devices should a device")
    public void shouldTestGetAllDevices() {
        //given
        List<Device> devices = generator.objects(Device.class, 1)
                .collect(Collectors.toList());
        given(deviceRepository.findAll()).willReturn(devices);
        //when
        List<Device> allDevices = sut.getAllDevices();
        //then
        assertThat(allDevices).hasSize(1).isSameAs(devices);
    }

    @Test
    @DisplayName("Get all devices should return empty")
    public void shouldReturnEmptyGetAllDevices() {
        //given
        given(deviceRepository.findAll()).willReturn(new ArrayList<>());
        //when
        List<Device> allDevices = sut.getAllDevices();
        //then
        assertThat(allDevices).hasSize(0);
    }

    @Test
    @DisplayName("Get devices by Id should return a device")
    public void shouldTestGetDeviceById() {
        //given
        Device device = generator.nextObject(Device.class);
        given(deviceRepository.findById(device.id())).willReturn(Optional.of(device));
        //when
        Device deviceById = sut.findDeviceById(device.id());
        //then
        assertThat(deviceById).isNotNull().isEqualTo(device);
    }

    @Test
    @DisplayName("Get devices by Id should return resource not found")
    public void shouldFailGetDeviceById() {
        //given
        given(deviceRepository.findById("invalid")).willReturn(Optional.empty());
        //when
        Throwable exception = assertThrows(ResourceNotFound.class, () ->
                sut.findDeviceById("invalid"));
        //then
        assertThat(exception).isInstanceOf(ResourceNotFound.class)
                .hasMessageContaining("Device with ID invalid not found");
    }

    @Test
    @DisplayName("Get devices by deviceUid should return a device")
    public void shouldTestGetDeviceByDeviceUid() {
        //given
        Device device = generator.nextObject(Device.class);
        given(deviceRepository.findByDeviceUid(device.deviceUid())).willReturn(Optional.of(device));
        //when
        Device deviceById = sut.findDeviceByDeviceUid(device.deviceUid());
        //then
        assertThat(deviceById).isNotNull().isEqualTo(device);
    }

    @Test
    @DisplayName("Get devices by deviceUid should return resource not found")
    public void shouldFailGetDeviceByDeviceUid() {
        //given
        given(deviceRepository.findByDeviceUid("invalid")).willReturn(Optional.empty());
        //when
        Throwable exception = assertThrows(ResourceNotFound.class, () ->
                sut.findDeviceByDeviceUid("invalid"));
        //then
        assertThat(exception).isInstanceOf(ResourceNotFound.class)
                .hasMessageContaining("Device with ID invalid not found");
    }

    @Test
    @DisplayName("Delete device by id")
    public void shouldDeleteDeviceSuccess() {
        //given
        doNothing().when(deviceRepository).deleteById("1");
        //when
        sut.deleteDevice("1");
        verify(deviceRepository).deleteById("1");
    }

    @Test
    @DisplayName("Create device should return success response")
    public void shouldTestCreateDevice() {
        //given
        Device device = generator.nextObject(Device.class);
        given(deviceRepository.findByDeviceUid(device.deviceUid())).willReturn(Optional.empty());
        given(deviceRepository.save(device)).willReturn(device);
        //when
        Device sutDevice = sut.createDevice(device);
        //then
        assertThat(sutDevice)
                .isNotNull()
                .extracting(Device::deviceUid, Device::customerId)
                .containsExactly(device.deviceUid(), device.customerId());
    }

    @Test
    @DisplayName("Update device should return success response")
    public void shouldUpdateDeviceReturnSuccess() {
        //given
        Device device = generator.nextObject(Device.class);
        given(deviceRepository.findById(device.id())).willReturn(Optional.of(device));
        given(deviceRepository.save(device)).willReturn(device);
        //when
        Device sutDevice = sut.updateDevice(device);
        //then
        assertThat(sutDevice)
                .isNotNull()
                .extracting(Device::deviceUid, Device::customerId)
                .containsExactly(device.deviceUid(), device.customerId());
    }

    @Test
    @DisplayName("Update device should return resource not found")
    public void shouldUpdateDeviceReturnError() {
        //given
        Device device = generator.nextObject(Device.class);
        given(deviceRepository.findById(device.id())).willReturn(Optional.empty());
        //when
        Throwable sutDevice = assertThrows(ResourceNotFound.class, () -> sut.updateDevice(device));
        //then
        assertThat(sutDevice)
                .isInstanceOf(ResourceNotFound.class).hasMessageContaining(String.format("Device with ID %s not found", device.id()));
    }

}
