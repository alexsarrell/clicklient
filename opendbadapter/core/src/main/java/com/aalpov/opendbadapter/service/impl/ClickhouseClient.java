package com.aalpov.opendbadapter.service.impl;

import com.aalpov.opendbadapter.Row;
import com.aalpov.opendbadapter.service.Converter;
import com.aalpov.opendbadapter.service.DatabaseClient;
import com.aalpov.opendbadapter.service.DatabaseContext;
import com.aalpov.opendbadapter.table.ClickhouseTable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ClickhouseClient implements DatabaseClient {

  DatabaseContext<ClickhouseTable> context;

  private final ExecutorService queryExecutor;

  public ClickhouseClient(DatabaseContext<ClickhouseTable> context, ExecutorService queryExecutor) {
    this.context = context;
    this.queryExecutor = queryExecutor;
  }

  public ClickhouseClient(DatabaseContext<ClickhouseTable> context) {
    this.context = context;
    this.queryExecutor = Executors.newFixedThreadPool(
            10, new ThreadFactoryBuilder()
                    .setNameFormat("ClickhouseClient-thread-%d")
                    .setPriority(Thread.NORM_PRIORITY)
                    .build()
    );
  }


  @Override
  public void insert(Object row) {
    ClickhouseTable mirror = context.getMirror(row.getClass());

    queryExecutor.submit(() -> {
      try {
        var fields = convertToMap(row, mirror);
        context.insert(new Row(fields), mirror);
      } catch (IllegalAccessException ex) {
        ex.printStackTrace();
      }
    });
  }

  @Override
  public void batchInsert(Collection<Object> rows) {
    Assert.notEmpty(rows, "Batch for bulk insert must not be empty");

    var first = rows.iterator().next().getClass();

    ClickhouseTable mirror = context.getMirror(first);
    for (Object obj : rows) {
      if (!first.isAssignableFrom(obj.getClass())) {
        throw new IllegalArgumentException("Rows for batch insert must have the same type " + rows);
      }
      Assert.notNull(obj, "Row for batch insert must not be null " + obj);
    }
    queryExecutor.submit(() -> batchInsert(rows, mirror));
  }

  private void batchInsert(Collection<Object> rows, ClickhouseTable mirror) {
    try {
      var rows_ =
              rows.stream()
                      .map(
                              row -> {
                                try {
                                  return new Row(convertToMap(row, mirror));
                                } catch (IllegalAccessException e) {
                                  throw new RuntimeException(e);
                                }
                              })
                      .toList();
      context.batchInsert(rows_, mirror);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void batchInsert(@NotNull Object... rows) {
    batchInsert(Arrays.stream(rows).toList());
  }

  private Map<String, Object> convertToMap(Object obj, ClickhouseTable mirror) throws IllegalAccessException {
    Map<String, Object> map = new HashMap<>();
    Class<?> clazz = obj.getClass();

    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      map.put(field.getName(), withConversion(field.getName(), field.get(obj), mirror));
    }

    return map;
  }

  private Object withConversion(String name, Object field, ClickhouseTable mirror) {
    if (mirror.getConverters().containsKey(name)) {
      Converter converter = mirror.getConverters().get(name);
      return converter.convert(field);
    } else {
      return field;
    }
  }
}
