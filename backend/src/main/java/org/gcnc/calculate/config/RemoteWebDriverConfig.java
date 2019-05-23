package org.gcnc.calculate.config;

import org.gcnc.calculate.model.Properties;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

@Configuration
public class RemoteWebDriverConfig {

    @Autowired
    Properties properties;

    private final Logger logger = LoggerFactory.getLogger(RemoteWebDriverConfig.class);

    @Bean
    public RemoteWebDriver getRemoteWebDriver() throws MalformedURLException {
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments(Arrays.asList("headless", "no-sandbox", "window-size=1200x800"));
            logger.info("Initializing remote web driver at url {}", properties.getSeleniumUrl());
            return new RemoteWebDriver(new URL(properties.getSeleniumUrl()), options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
