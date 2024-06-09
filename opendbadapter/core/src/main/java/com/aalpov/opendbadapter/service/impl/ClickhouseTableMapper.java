package com.aalpov.opendbadapter.service.impl;

import com.aalpov.opendbadapter.Column;
import com.aalpov.opendbadapter.Expression;
import com.aalpov.opendbadapter.annotations.*;
import com.aalpov.opendbadapter.keys.Engine;
import com.aalpov.opendbadapter.keys.Order;
import com.aalpov.opendbadapter.keys.Partition;
import com.aalpov.opendbadapter.service.Converter;
import com.aalpov.opendbadapter.service.TableMapper;
import com.aalpov.opendbadapter.table.ClickhouseTable;
import com.aalpov.opendbadapter.table.TableName;
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

  private List<Converter> converters;

  public ClickhouseTableMapper(
          List<Converter> converters
  ) {
    this.converters = converters;
  }

  public ClickhouseTableMapper() {}

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
            PrimaryKey keyAnnotation = clazz.getAnnotation(PrimaryKey.class);

            Field[] fields = clazz.getDeclaredFields();

            Expression orderedExpression = buildExpression(orderedByAnnotation);
            Expression partitionByExpression = buildExpression(partitionByAnnotation);
            Expression primaryKeyExpression = buildExpression(keyAnnotation);

            T table =
                buildTable(
                    new TableName(tableAnnotation.name()),
                    fields,
                    new Order(orderedExpression),
                    primaryKeyExpression != null ? new com.aalpov.opendbadapter.keys.PrimaryKey(primaryKeyExpression) : null,
                    new Engine(tableAnnotation.engine()),
                    partitionByExpression != null ? new Partition(partitionByExpression) : null,
                    clazz);

            tables.add(table);

            logger.info("New table has been registered: " + table.toString());
          } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
          }
        });
    return tables;
  }

  @Override
  public T buildTable(
          TableName name, Field[] fields, Order order, com.aalpov.opendbadapter.keys.PrimaryKey key,
          Engine engine,
          Partition partition, Class<?> mirror) {
    ServiceLoader<DbType> loader = ServiceLoader.load(DbType.class);
    var clickhouseTypes =
        loader.stream().map(ServiceLoader.Provider::get).collect(Collectors.toList());

    HashMap<String, Converter<?, ?, ?>> converters = new HashMap<>();

    List<Column> columns =
        Arrays.stream(fields)
            .map(field -> new Column(field.getName(), getDbType(clickhouseTypes, field, converters)))
            .collect(Collectors.toList());

    return (T) new ClickhouseTable(name.value(), columns, engine, order, key, partition, converters, mirror);
  }

  private DbType<?> getDbType(List<DbType> types, Field field, HashMap<String, Converter<?, ?, ?>> converters) {
    var result = types.stream().filter(type -> field.getType() == type.getJavaType()).findFirst();

    if (result.isPresent()) {
      return result.get();
    } else if (hasCustomConverters(field)) {
      return convert(field, converters);
    } else {
      throw new ClassCastException("Cannot cast property " + field.getName() + " to any DbType");
    }
  }

  private Expression buildExpression(Annotation annotation) {
    if (annotation instanceof OrderedBy orderedBy) {
      return new Expression(orderedBy.expression(), orderedBy.columns());
    } else if (annotation instanceof PartitionBy partitionBy) {
      return new Expression(partitionBy.expression(), partitionBy.columns());
    } else if (annotation instanceof PrimaryKey primaryKey) {
      return new Expression(primaryKey.expression(), primaryKey.columns());
    } else {
      return null;
    }
  }

  private Boolean hasCustomConverters(Field field) {

    return field.getAnnotation(Convert.class) != null;
  }

  private DbType<?> convert(Field field, HashMap<String, Converter<?, ?, ?>> convertersMap) {
    Convert convert = field.getAnnotation(Convert.class);

    if (convert != null && !converters.isEmpty()) {
      Converter converter = converters.stream()
              .filter(cnv -> cnv.getClass().equals(convert.converter())).toList().get(0);

      convertersMap.put(field.getName(), converter);
      return converter.convertType(field);
    } else {
      throw new RuntimeException("No beans of type Converter for field " + field + " are registered");
    }
  }
}
