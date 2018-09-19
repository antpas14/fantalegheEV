package org.gcnc.calculate.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "properties")
@Component("properties")
@Data
public class Properties {
    private String baseUrl;
    private String calendarSuffix;

    private String seleniumUrl;
}
