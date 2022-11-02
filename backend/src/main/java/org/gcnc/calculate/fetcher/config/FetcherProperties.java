package org.gcnc.calculate.fetcher.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "properties.fetcher")
@Component
@Data
public class FetcherProperties {
    private String url;
}
