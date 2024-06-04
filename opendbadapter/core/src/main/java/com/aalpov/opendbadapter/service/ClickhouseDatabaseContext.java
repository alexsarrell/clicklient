package com.aalpov.opendbadapter.service;

import com.aalpov.opendbadapter.Row;
import com.aalpov.opendbadapter.table.ClickhouseTable;
import com.zaxxer.hikari.HikariDataSource;

import java.util.List;

public class ClickhouseDatabaseContext<T extends ClickhouseTable>
    extends AbstractDatabaseContext<T> {

  public ClickhouseDatabaseContext(HikariDataSource dataSource) {
    super(dataSource);
  }

  public ClickhouseDatabaseContext() {
    super();
  }

  @Override
  public String createTable(T table) {
    return "CREATE TABLE IF NOT EXISTS " + table.toSqlString();
  }

  @Override
  public String insertQuery(Row row, T table) {
    return "INSERT INTO " + table.getName() + " " + row.keysToSqlString();
  }

  @Override
  public String batchInsertQuery(List<Row> rows, T table) {
    StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + table.getName() + " ");
    queryBuilder.append(rows.get(0).keysToSqlString());

    for (int i = 0; i < rows.size(); i++) {
      var row = rows.get(i);
      queryBuilder.append(row.valuesToSqlString());
      if (i < rows.size() - 1) {
        queryBuilder.append(',');
      }
    }
    return queryBuilder.toString();
  }
}
