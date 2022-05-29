package com.smart.device.devicebackend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.device.devicebackend.AbstractIntegrationTest;
import com.smart.device.devicebackend.model.Image;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.cloud.contract.spec.internal.MediaTypes;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Import(AbstractIntegrationTest.AwsTestConfig.class)
public class AwsS3ControllerTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Get all devices")
    public void shouldSuccessFileUpload() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "sample.png",
                MediaType.IMAGE_PNG_VALUE, "aws".getBytes());
        mockMvc.perform(multipart("/api/v1/s3/upload")
                        .file(multipartFile)
                        .param("imageName", "aws")
                        .param("imageId", "aws")
                        .param("description", "aws description"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.APPLICATION_JSON))
                .andExpect(jsonPath("imageName").value("aws"))
                .andExpect(jsonPath("description").value("aws description"))
                .andExpect(jsonPath("filePath").value("sample.png"));
    }

    @Test
    @DisplayName("Get all devices")
    public void shouldSuccessFileDownload() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "sample.png",
                MediaType.IMAGE_PNG_VALUE, "aws".getBytes());
        MvcResult mvcResult = mockMvc.perform(multipart("/api/v1/s3/upload")
                        .file(multipartFile)
                        .param("imageName", "aws")
                        .param("imageId", "aws")
                        .param("description", "aws description"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.APPLICATION_JSON)).andReturn();

        Image image = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Image.class);

        mockMvc.perform(get("/api/v1/s3/image/download/"+image.imageName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.IMAGE_JPEG))
                .andDo(MockMvcResultHandlers.print());
    }
}
