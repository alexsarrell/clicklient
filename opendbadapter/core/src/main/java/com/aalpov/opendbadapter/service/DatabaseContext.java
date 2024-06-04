package com.aalpov.opendbadapter.service;

import com.aalpov.opendbadapter.Row;
import com.aalpov.opendbadapter.configuration.DatabaseProperties;
import com.aalpov.opendbadapter.table.AbstractTable;
import java.util.Collection;
import java.util.List;

public interface DatabaseContext<T extends AbstractTable> {

  void insert(Row row, T table);

  void batchInsert(List<Row> rows, T table);

  void executeQuery(String query);

  void register(Collection<T> tables);

  void initialize(DatabaseProperties properties);

  T getMirror(Class<?> clazz);
}
