package com.smart.device.devicebackend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.device.devicebackend.AbstractIntegrationTest;
import com.smart.device.devicebackend.model.Device;
import com.smart.device.devicebackend.model.DeviceOrderEvent;
import com.smart.device.devicebackend.repository.DeviceOrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.contract.spec.internal.MediaTypes;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.awaitility.Awaitility.given;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@Import(AbstractIntegrationTest.AwsTestConfig.class)
public class AwsSQSControllerTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Autowired
    private DeviceOrderRepository deviceOrderRepository;

    @Test
    public void getAllDeviceOrder() {
        assertThat(deviceOrderRepository.findAll()).hasSize(0);
    }

    @Test
    @DisplayName("Create device by deviceId")
    public void shouldReturnSuccessForSendEvent() throws Exception {
        DeviceOrderEvent deviceOrderEvent = new DeviceOrderEvent("3", "Siemens", "Siemens", "hardwareUid", true);
        mockMvc.perform(post("/api/v1/sqs/publish/device/order/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(deviceOrderEvent)))
                .andExpect(status().isCreated());
//        List<DeviceOrderEvent> all = deviceOrderRepository.findAll();
//        assertThat(all).hasSize(1);
        given()
//                .ignoreException(AmazonS3Exception.class)
                .await()
                .atMost(20, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(deviceOrderRepository.findAll()).hasSize(1));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
