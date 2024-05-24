package com.aalpov.opendbadapter.service.impl;

import com.aalpov.opendbadapter.Expression;
import com.aalpov.opendbadapter.annotations.OrderedBy;
import com.aalpov.opendbadapter.annotations.Table;
import com.aalpov.opendbadapter.keys.Order;
import com.aalpov.opendbadapter.model.TableName;
import com.aalpov.opendbadapter.service.DatabaseContext;
import com.aalpov.opendbadapter.table.ClickhouseTable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

class ClickhouseTablesRegistrar {

  ClassPathScanningCandidateComponentProvider scanner;

  ClickhouseTableMapper mapper;

  DatabaseContext context;

  ClickhouseTablesRegistrar(ClickhouseTableMapper tableMapper, DatabaseContext context) {
    this.scanner = new ClassPathScanningCandidateComponentProvider(false);
    this.mapper = tableMapper;
    this.context = context;
  }

  public void scanAndRegister(String[] basePackages) {
    Set<BeanDefinition> definitions = new HashSet<>();

    for (String basePackage : basePackages) {
      definitions.addAll(findTableAnnotatedClasses(basePackage));
    }

    registerTables(definitions);
  }

  private Set<BeanDefinition> findTableAnnotatedClasses(String basePackage) {

    scanner.addIncludeFilter(new AnnotationTypeFilter(Table.class));

    return scanner.findCandidateComponents(basePackage);
  }

  private void registerTables(Set<BeanDefinition> definitions) {
    List<ClickhouseTable> tables = new ArrayList<>();
    definitions.forEach(
        bd -> {
          String className = bd.getBeanClassName();
          try {
            Class<?> clazz = Class.forName(className);

            var tableAnnotation = clazz.getAnnotation(Table.class);
            var expressionAnnotation = clazz.getAnnotation(OrderedBy.class);

            var fields = clazz.getDeclaredFields();

            Expression expression;
            if (expressionAnnotation != null) {
              if (expressionAnnotation.columns().length != 0) {
                expression = new Expression(expressionAnnotation.columns());
              } else {
                expression = new Expression(expressionAnnotation.expression());
              }
            } else {
              expression = new Expression("");
            }
            // var partitionFields = annotatedBy(PartitionBy.class, fields);
            // var orderingFields = annotatedBy(OrderedBy.class, fields);
            // var indexes = annotatedBy(Indexed.class, fields);
            ClickhouseTable table =
                mapper.register(
                    new TableName(tableAnnotation.name()), fields, new Order(expression), clazz);

            tables.add(table);

            System.out.println("New table has been registered: " + table.toString());
          } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
          }
        });

    context.register(tables);
  }

  private <T extends Annotation> Field[] annotatedBy(Class<T> annotation, Field[] fields) {
    return (Field[])
        Arrays.stream(fields).filter(field -> field.isAnnotationPresent(annotation)).toArray();
  }
}
