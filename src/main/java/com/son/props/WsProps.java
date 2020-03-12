package com.son.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ws.server")
@Data
public class WsProps {
    private Integer port;
    private String host;
}
