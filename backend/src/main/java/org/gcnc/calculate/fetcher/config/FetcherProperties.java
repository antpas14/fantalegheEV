package org.gcnc.calculate.fetcher.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "properties.fetcher")
@Component
public record FetcherProperties(String url) { }

