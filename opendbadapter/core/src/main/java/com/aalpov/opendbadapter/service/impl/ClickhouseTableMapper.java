package com.aalpov.opendbadapter.service.impl;

import com.aalpov.opendbadapter.Column;
import com.aalpov.opendbadapter.Expression;
import com.aalpov.opendbadapter.annotations.OrderedBy;
import com.aalpov.opendbadapter.annotations.PartitionBy;
import com.aalpov.opendbadapter.annotations.Table;
import com.aalpov.opendbadapter.engine.MergeTreeEngine;
import com.aalpov.opendbadapter.keys.Order;
import com.aalpov.opendbadapter.keys.Partition;
import com.aalpov.opendbadapter.service.TableMapper;
import com.aalpov.opendbadapter.table.ClickhouseTable;
import com.aalpov.opendbadapter.table.TableName;
import com.aalpov.opendbadapter.type.DbFloat32;
import com.aalpov.opendbadapter.type.DbType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ClickhouseTableMapper<T extends ClickhouseTable> implements TableMapper<T> {

  private static final Logger logger = LoggerFactory.getLogger(ClickhouseTableMapper.class);

  @Override
  public Collection<T> mapTables(Set<BeanDefinition> definitions) {
    ArrayList<T> tables = new ArrayList<>();
    definitions.forEach(
        bd -> {
          String className = bd.getBeanClassName();
          try {
            Class<?> clazz = Class.forName(className);

            Table tableAnnotation = clazz.getAnnotation(Table.class);
            OrderedBy orderedByAnnotation = clazz.getAnnotation(OrderedBy.class);
            PartitionBy partitionByAnnotation = clazz.getAnnotation(PartitionBy.class);

            Field[] fields = clazz.getDeclaredFields();

            Expression orderedExpression = buildExpression(orderedByAnnotation);
            Expression partitionByExpression = buildExpression(partitionByAnnotation);

            ClickhouseTable table =
                buildTable(
                    new TableName(tableAnnotation.name()),
                    fields,
                    new Order(orderedExpression),
                    partitionByExpression != null ? new Partition(partitionByExpression) : null,
                    clazz);

            tables.add((T) table);

            logger.info("New table has been registered: " + table.toString());
          } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
          }
        });
    return tables;
  }

  @Override
  public T buildTable(
          TableName name, Field[] fields, Order order, Partition partition, Class<?> mirror) {
    ServiceLoader<DbType> loader = ServiceLoader.load(DbType.class);
    var clickhouseTypes =
        loader.stream().map(ServiceLoader.Provider::get).collect(Collectors.toList());

    List<Column> columns =
        Arrays.stream(fields)
            .map(field -> new Column(field.getName(), getDbType(clickhouseTypes, field)))
            .collect(Collectors.toList());

    return (T) new ClickhouseTable(name.value(), columns, new MergeTreeEngine(), order, partition, mirror);
  }

  private DbType<?> getDbType(List<DbType> types, Field field) {
    var result = types.stream().filter(type -> field.getType() == type.getJavaType()).findFirst();

    if (result.isPresent()) {
      return result.get();
    } else if (hasCustomConverters(field)) {
      return convert(field);
    } else {
      throw new ClassCastException("Cannot cast property " + field.getName() + " to any DbType");
    }
  }

  private <A extends Annotation> Field[] annotatedBy(Class<A> annotation, Field[] fields) {
    return Arrays.stream(fields)
        .filter(field -> field.isAnnotationPresent(annotation))
        .toArray(Field[]::new);
  }

  private Expression buildExpression(Annotation annotation) {
    if (annotation instanceof OrderedBy orderedBy) {
      return new Expression(orderedBy.expression(), orderedBy.columns());
    } else if (annotation instanceof PartitionBy partitionBy) {
      return new Expression(partitionBy.expression(), partitionBy.columns());
    } else {
      return null;
    }
  }

  private Boolean hasCustomConverters(Field field) {
    // TODO: 31/03/2024 apply custom conversions
    return false;
  }

  private DbType<?> convert(Field field) {
    // TODO: 31/03/2024 apply custom conversions
    return new DbFloat32();
  }
}
