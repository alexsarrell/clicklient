package com.aalpov.opendbadapter.service.impl;

import com.aalpov.opendbadapter.annotations.Table;
import com.aalpov.opendbadapter.service.DatabaseContext;
import com.aalpov.opendbadapter.service.TablesRegistrar;
import com.aalpov.opendbadapter.table.ClickhouseTable;
import java.util.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class ClickhouseTablesRegistrar<T extends ClickhouseTable> implements TablesRegistrar<T> {

  ClassPathScanningCandidateComponentProvider scanner;

  ClickhouseTableMapper<T> mapper;

  DatabaseContext<T> context;

  public ClickhouseTablesRegistrar(
      ClickhouseTableMapper<T> tableMapper, DatabaseContext<T> context) {
    this.scanner = new ClassPathScanningCandidateComponentProvider(false);
    this.mapper = tableMapper;
    this.context = context;
  }

  @Override
  public void scanAndRegister(String[] basePackages) {
    Set<BeanDefinition> definitions = new HashSet<>();

    for (String basePackage : basePackages) {
      definitions.addAll(findTableAnnotatedClasses(basePackage));
    }

    registerTables(definitions);
  }

  @Override
  public DatabaseContext<T> getContext() {
    return context;
  }

  private Set<BeanDefinition> findTableAnnotatedClasses(String basePackage) {

    scanner.addIncludeFilter(new AnnotationTypeFilter(Table.class));

    return scanner.findCandidateComponents(basePackage);
  }

  private void registerTables(Set<BeanDefinition> definitions) {
    context.register(mapper.mapTables(definitions));
  }
}
