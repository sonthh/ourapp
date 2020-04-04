package com.son.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloudinary")
@Data
public class CloudinaryProps {
    private String cloudName;
    private String apiSecret;
    private String apiKey;
}
