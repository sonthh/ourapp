package com.son.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "firebase.fcm")
@Data
public class FcmProps {
    private String configFile;
}
