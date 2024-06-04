package com.aalpov.opendbadapter.service.impl;

import com.aalpov.opendbadapter.Row;
import com.aalpov.opendbadapter.service.ClickhouseClient;
import com.aalpov.opendbadapter.service.DatabaseContext;
import com.aalpov.opendbadapter.table.ClickhouseTable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

public class BaseClickhouseClient implements ClickhouseClient {

  DatabaseContext<ClickhouseTable> context;

  private final ExecutorService queryExecutor = Executors.newFixedThreadPool(
          10, new ThreadFactoryBuilder()
                  .setNameFormat("ClickhouseClient-thread-%d")
                  .setPriority(Thread.NORM_PRIORITY)
                  .build()
  );

  public BaseClickhouseClient(DatabaseContext<ClickhouseTable> context) {
    this.context = context;
  }

  @Override
  public void insert(Object row) {
    ClickhouseTable mirror = context.getMirror(row.getClass());

    queryExecutor.submit(() -> {
      try {
        var fields = convertToMap(row);
        context.insert(new Row(fields), mirror);
      } catch (IllegalAccessException ex) {
        ex.printStackTrace();
      }
    });
  }

  @Override
  public void batchInsert(Collection<Object> rows) {
    Assert.notEmpty(rows, "Batch for bulk insert must not be empty");

    ClickhouseTable mirror = context.getMirror(rows.iterator().next().getClass());
    queryExecutor.submit(() -> batchInsert(rows, mirror));
  }

  private void batchInsert(Collection<Object> rows, ClickhouseTable mirror) {
    try {
      var rows_ =
              rows.stream()
                      .map(
                              row -> {
                                try {
                                  return new Row(convertToMap(row));
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

  private Map<String, Object> convertToMap(Object obj) throws IllegalAccessException {
    Map<String, Object> map = new HashMap<>();
    Class<?> clazz = obj.getClass();

    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      map.put(field.getName(), field.get(obj));
    }

    return map;
  }
}
