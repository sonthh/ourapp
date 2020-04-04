package com.son.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.son.props.CloudinaryProps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({CloudinaryProps.class})
@Slf4j
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(CloudinaryProps cloudinaryProps) {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudinaryProps.getCloudName(),
            "api_key", cloudinaryProps.getApiKey(),
            "api_secret", cloudinaryProps.getApiSecret()
        ));
    }
}
