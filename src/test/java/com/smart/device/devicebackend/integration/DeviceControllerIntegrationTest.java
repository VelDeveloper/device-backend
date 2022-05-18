package com.smart.device.devicebackend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.device.devicebackend.model.Device;
import com.smart.device.devicebackend.repository.DeviceRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.spec.internal.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest
public class DeviceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeviceRepository deviceRepository;
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

    @AfterEach
    void cleanUp() {
        this.deviceRepository.deleteAll();
    }

    @Test
    @DisplayName("Get all devices")
    public void shouldReturnAllDevices() throws Exception {
        mockMvc.perform(get("/api/v1/device/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Check delete devices")
    public void shouldReturnSuccessForDeleteDevices() throws Exception {
        mockMvc.perform(delete("/api/v1/device/1"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/device/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("Get device by deviceId")
    public void shouldReturnSuccessForGetDeviceById() throws Exception {
        mockMvc.perform(get("/api/v1/device/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.APPLICATION_JSON))
                .andExpect(jsonPath("deviceUid").value("philips"))
                .andExpect(jsonPath("customerId").value("customerId"));
    }

    @Test
    @DisplayName("Update device by deviceId")
    public void shouldReturnSuccessForUpdateDeviceById() throws Exception {
        Device device = new Device("2", "philips", "philips", "hardwareUid", "productId", false, "45678-abcd-efgh-thyu");
        mockMvc.perform(put("/api/v1/device/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(device)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaTypes.APPLICATION_JSON))
                .andExpect(jsonPath("deviceUid").value("philips"))
                .andExpect(jsonPath("customerId").value("45678-abcd-efgh-thyu"));
    }

    @Test
    @DisplayName("Create device by deviceId")
    public void shouldReturnSuccessForCreateDevice() throws Exception {
        Device device = new Device("3", "Siemens", "Siemens", "hardwareUid", "productId", false, "customerId");
        mockMvc.perform(post("/api/v1/device")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(device)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.APPLICATION_JSON))
                .andExpect(jsonPath("deviceUid").value("Siemens"))
                .andExpect(jsonPath("customerId").value("customerId"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
