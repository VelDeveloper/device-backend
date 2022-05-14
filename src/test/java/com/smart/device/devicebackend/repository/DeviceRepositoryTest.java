package com.smart.device.devicebackend.repository;

import com.smart.device.devicebackend.model.Device;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class DeviceRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.3");

    @DynamicPropertySource
    static void setProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeAll
    static void init() {
        mongoDBContainer.start();
    }

    @BeforeEach
    void initialize() {
        this.deviceRepository.save(new Device("1", "samsung","samsung", "hardwareUid","productId",false,"customerId"));
        this.deviceRepository.save(new Device("2", "philips","philips", "hardwareUid","productId",false,"customerId"));
    }
    @Autowired
    private DeviceRepository deviceRepository;

    @AfterEach
    void cleanUp() {
        this.deviceRepository.deleteAll();
    }

    @Test
    void testContainerRunning() {
        assertThat(mongoDBContainer.isRunning()).isTrue();
    }

    @Test
    void shouldReturnListOfDevice() {
        List<Device> devices = this.deviceRepository.findAll();
        assertThat(devices).hasSize(2);
    }

    @Test
    void shouldReturnDeviceByUid() {
        Optional<Device> philips = this.deviceRepository.findByDeviceUid("philips");
        assertThat(philips)
                .isPresent()
                .map(Device::deviceUid)
                .hasValue("philips");
    }

    @Test
    void shouldReturnDeviceById() {
        Optional<Device> samsung = this.deviceRepository.findById("1");
        assertThat(samsung)
                .isPresent()
                .map(Device::deviceUid)
                .hasValue("samsung");
    }

    @Test
    void shouldReturnemptyDeviceById() {
        Optional<Device> empty = this.deviceRepository.findById("3");
        assertThat(empty)
                .isEmpty();
    }
}
