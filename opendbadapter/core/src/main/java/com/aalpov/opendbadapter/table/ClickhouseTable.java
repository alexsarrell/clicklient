package com.aalpov.opendbadapter.table;

import com.aalpov.opendbadapter.Column;
import com.aalpov.opendbadapter.keys.Engine;
import com.aalpov.opendbadapter.keys.Order;
import com.aalpov.opendbadapter.keys.Partition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.aalpov.opendbadapter.keys.PrimaryKey;
import com.aalpov.opendbadapter.service.Converter;
import org.jetbrains.annotations.NotNull;

public class ClickhouseTable implements Table {

  final Engine engine;

  final String name;

  List<Column> columns;

  Class<?> mirror;

  final Order order;

  final Map<String, Converter<?, ?, ?>> converters;

  final Optional<PrimaryKey> primaryKey;

  final Optional<Partition> partition;

  public ClickhouseTable(
      @NotNull String name,
      @NotNull List<Column> columns,
      @NotNull Engine engine,
      @NotNull Order order,
      PrimaryKey key,
      Partition partition,
      Map<String, Converter<?, ?, ?>> converters,
      @NotNull Class<?> mirror) {

    this.name = name;
    this.engine = engine;
    this.columns = columns;
    this.mirror = mirror;
    this.order = order;
    this.converters = converters;
    this.primaryKey = Optional.ofNullable(key);
    this.partition = Optional.ofNullable(partition);
  }

  public ClickhouseTable(
          @NotNull String name,
          @NotNull List<Column> columns,
          @NotNull Engine engine,
          @NotNull Order order,
          PrimaryKey key,
          Partition partition,
          @NotNull Class<?> mirror) {

    this.name = name;
    this.engine = engine;
    this.columns = columns;
    this.mirror = mirror;
    this.order = order;
    this.converters = new HashMap<>();
    this.primaryKey = Optional.ofNullable(key);
    this.partition = Optional.ofNullable(partition);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Class<?> getMirror() {
    return mirror;
  }

  @Override
  public String columns() {
    var names = columns.stream().map(Column::getName).toList();
    return "(" + String.join(",", names) + ")";
  }

  public Map<String, Converter<?, ?, ?>> getConverters() {
    return this.converters;
  }

  public String toSqlString() {
    final StringBuilder sb = new StringBuilder();
    sb.append(name).append("(");
    for (int i = 0; i < columns.size(); i++) {
      sb.append(columns.get(i).getName()).append(" ").append(columns.get(i).getType().toSqlName());
      if (columns.size() - 1 > i) {
        sb.append(',');
      }
    }
    sb.append(')');
    sb.append("\n ENGINE = ").append(engine);
    sb.append("\n PRIMARY KEY ").append(primaryKey.toString());
    sb.append("\n ORDER BY ").append(order.toString());
    partition.ifPresent(pt -> sb.append("\n PARTITION BY ").append(pt));
    return sb.toString();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("\nTable: ").append(name);
    sb.append(" {\n");
    for (Column column : columns) {
      sb.append("    ").append(column.toString()).append(";\n");
    }
    sb.append("}");
    return sb.toString();
  }
}
