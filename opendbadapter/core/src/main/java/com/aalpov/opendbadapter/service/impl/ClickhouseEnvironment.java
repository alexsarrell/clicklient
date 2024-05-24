package com.aalpov.opendbadapter.service.impl;

import com.aalpov.opendbadapter.configuration.DatabaseProperties;
import com.aalpov.opendbadapter.annotations.ClickhouseTablesScan;
import com.aalpov.opendbadapter.service.TableMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class ClickhouseEnvironment implements BeanDefinitionRegistryPostProcessor, ApplicationListener<ContextRefreshedEvent> {

    ClickhouseTablesRegistrar registrar;

    private final ObjectProvider<DatabaseProperties> databaseProperties;

    public ClickhouseEnvironment(TableMapper mapper, ObjectProvider<DatabaseProperties> databaseProperties) {
        this.registrar = new ClickhouseTablesRegistrar((ClickhouseTableMapper) mapper, new ClickhouseDatabaseContext());
        this.databaseProperties = databaseProperties;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NotNull BeanDefinitionRegistry registry) throws BeansException {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AnnotationTypeFilter(ClickhouseTablesScan.class));
        String basePackage = "";
        for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
            String className = bd.getBeanClassName();

            try {
                Class<?> clazz = Class.forName(className);
                ClickhouseTablesScan annotation = clazz.getAnnotation(ClickhouseTablesScan.class);
                if (annotation != null) {
                    String[] basePackages = annotation.basePackages();
                    registrar.scanAndRegister(basePackages);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public BaseClickhouseClient getClient() {
        return new BaseClickhouseClient(registrar.context);
    }

    @Override
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory) throws BeansException {}

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        registrar.context.initialize(databaseProperties.getObject());
    }
}