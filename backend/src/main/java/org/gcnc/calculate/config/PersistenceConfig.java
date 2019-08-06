package org.gcnc.calculate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by antonellopastena on 2019-04-06
 */
@Order(3)
@Configuration
@EnableTransactionManagement
@ComponentScan
public class PersistenceConfig {

  @Autowired
  private DataSource dataSource;

  @Autowired
  private Props props;

  @Autowired
  private Properties properties;

  @Bean
  public EntityManagerFactory entityManagerFactory() {
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(false);
    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setDataSource(dataSource);
    factory.setPackagesToScan(props.getPackagesToScan());
    factory.setJpaProperties(properties);
    factory.afterPropertiesSet();
    return factory.getObject();
  }


}

