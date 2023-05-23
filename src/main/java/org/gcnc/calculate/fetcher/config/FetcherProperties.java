package org.gcnc.calculate.fetcher.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "properties.fetcher")
@Component
@Getter
@Setter
public class FetcherProperties {
    private String url;
}