package org.gcnc.calculate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "properties.calculate")
@Component
@Data
public class CalculateProperties {
    private String baseUrl;
    private String calendarSuffix;
    private String rankingSuffix;
}
