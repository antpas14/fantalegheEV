package org.gcnc.calculate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by antonellopastena on 2019-04-06
 */
@Order(1)
@ConfigurationProperties(prefix = "props")
@Component
@Data
public class Props {
    private String url;
    private String hbm2ddlAuto;
    private String showSql;
    private String driverClassname;
    private String username;
    private String password;
    private String packagesToScan;
  }