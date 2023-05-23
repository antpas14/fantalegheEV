package org.gcnc.calculate.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "properties.calculate")
@Component
@Getter
@Setter
public class CalculateProperties {
    private String baseUrl;
    private String calendarSuffix;
    private String rankingSuffix;
}
