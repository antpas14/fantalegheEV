package org.gcnc.calculate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Created by antonellopastena on 2019-04-06
 */
@Order(2)
@Component
public class DbConfig {

    @Autowired
    private Props props;

    @Bean
    @Primary
    public DataSource dataSource() {
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName(props.getDriverClassname());
      dataSource.setUrl(props.getUrl());
      dataSource.setUsername(props.getUsername());
      dataSource.setPassword(props.getPassword());
      return dataSource;
    }

    @Bean
    @Primary
    public FlywayMigrationStrategy migrationStrategy() {
      FlywayMigrationStrategy strategy = flyway -> {
        flyway.setDataSource(dataSource());
        flyway.setLocations("classpath:db");
        flyway.migrate();
      };
      return strategy;
    }

}
