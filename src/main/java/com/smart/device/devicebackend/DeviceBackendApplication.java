package com.smart.device.devicebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DeviceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeviceBackendApplication.class, args);
    }

}
