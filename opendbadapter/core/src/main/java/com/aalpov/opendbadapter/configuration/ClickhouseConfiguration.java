package com.aalpov.opendbadapter.configuration;

import com.aalpov.opendbadapter.service.ClickhouseDatabaseContext;
import com.aalpov.opendbadapter.service.impl.BaseClickhouseClient;
import com.aalpov.opendbadapter.service.impl.ClickhouseAnnotationsRegistrar;
import com.aalpov.opendbadapter.service.impl.ClickhouseTableMapper;
import com.aalpov.opendbadapter.service.impl.ClickhouseTablesRegistrar;
import com.aalpov.opendbadapter.table.ClickhouseTable;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClickhouseConfiguration {

  @Bean
  public static ClickhouseTableMapper<ClickhouseTable> tableMapper() {
    return new ClickhouseTableMapper<>();
  }

  @Bean
  public static ClickhouseDatabaseContext<ClickhouseTable> context() {
    return new ClickhouseDatabaseContext<>();
  }

  @Bean
  public static ClickhouseTablesRegistrar<ClickhouseTable> tablesRegistrar(
      ClickhouseTableMapper<ClickhouseTable> mapper, ClickhouseDatabaseContext<ClickhouseTable> context) {
    return new ClickhouseTablesRegistrar<>(mapper, context);
  }

  @Bean
  public static BeanDefinitionRegistryPostProcessor clickhouseAnnotationsRegistrar(
      ClickhouseTablesRegistrar<ClickhouseTable> registrar, ObjectProvider<DatabaseProperties> databaseProperties) {
    return new ClickhouseAnnotationsRegistrar(registrar, databaseProperties);
  }

  @Bean
  public static BaseClickhouseClient clickhouseClient(
      @Qualifier("clickhouseAnnotationsRegistrar")
          BeanDefinitionRegistryPostProcessor environment) {
    if (environment instanceof ClickhouseAnnotationsRegistrar) {
      return ((ClickhouseAnnotationsRegistrar) environment).getClient();
    } else {
      throw new RuntimeException("No ClickhouseAnnotationsRegistrar are provided");
    }
  }
}
