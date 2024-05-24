package com.aalpov.opendbadapter.service.impl;

import com.aalpov.opendbadapter.Row;
import com.aalpov.opendbadapter.service.AbstractDatabaseContext;
import com.aalpov.opendbadapter.table.ClickhouseTable;

import java.util.List;

public class ClickhouseDatabaseContext extends AbstractDatabaseContext {

    @Override
    String createTable(ClickhouseTable table) {
        return "CREATE TABLE IF NOT EXISTS " + table.toSqlString();
    }

    @Override
    String insertQuery(Row row, ClickhouseTable table) {
        return "INSERT INTO " + table.getName() + " " + row.keysToSqlString();
    }

    @Override
    String batchInsertQuery(List<Row> rows, ClickhouseTable table) {
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
