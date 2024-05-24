package com.aalpov.opendbadapter.configuration;

import com.aalpov.opendbadapter.service.impl.BaseClickhouseClient;
import com.aalpov.opendbadapter.service.impl.ClickhouseEnvironment;
import com.aalpov.opendbadapter.service.impl.ClickhouseTableMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClickhouseConfiguration {

    @Bean
    public static ClickhouseTableMapper tableMapper() {
        return new ClickhouseTableMapper();
    }

    @Bean
    public static BeanDefinitionRegistryPostProcessor clickhouseEnvironment(
            ClickhouseTableMapper mapper,
            ObjectProvider<DatabaseProperties> databaseProperties
    ) {
        return new ClickhouseEnvironment(mapper, databaseProperties);
    }

    @Bean
    public static BaseClickhouseClient clickhouseClient(
            @Qualifier("clickhouseEnvironment") BeanDefinitionRegistryPostProcessor environment
            ) {
        if (environment instanceof ClickhouseEnvironment) {
            return ((ClickhouseEnvironment) environment).getClient();
        } else {
            throw new RuntimeException("No ClickhouseEnvironment are provided");
        }
    }
}