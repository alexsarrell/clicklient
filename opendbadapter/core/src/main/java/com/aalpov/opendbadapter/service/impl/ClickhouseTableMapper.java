package com.aalpov.opendbadapter.service.impl;

import com.aalpov.opendbadapter.Column;
import com.aalpov.opendbadapter.engine.MergeTreeEngine;
import com.aalpov.opendbadapter.keys.Order;
import com.aalpov.opendbadapter.model.TableName;
import com.aalpov.opendbadapter.service.TableMapper;
import com.aalpov.opendbadapter.table.ClickhouseTable;
import com.aalpov.opendbadapter.type.DbFloat32;
import com.aalpov.opendbadapter.type.DbType;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ClickhouseTableMapper implements TableMapper {

  @Override
  public ClickhouseTable register(TableName name, Field[] fields, Order order, Class<?> mirror) {
    ServiceLoader<DbType> loader = ServiceLoader.load(DbType.class);
    var clickhouseTypes =
        loader.stream().map(ServiceLoader.Provider::get).collect(Collectors.toList());

    List<Column> columns =
        Arrays.stream(fields)
            .map(field -> new Column(field.getName(), getDbType(clickhouseTypes, field)))
            .collect(Collectors.toList());

    return new ClickhouseTable(name.value(), columns, new MergeTreeEngine(), order, mirror);
  }

  private DbType<?> getDbType(List<DbType> types, Field field) {
    var result = types.stream().filter(type -> field.getType() == type.javaType).findFirst();

    if (result.isPresent()) {
      return result.get();
    } else if (hasCustomConverters(field)) {
      return convert(field);
    } else {
      throw new ClassCastException("Cannot cast property " + field.getName() + " to any DbType");
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
