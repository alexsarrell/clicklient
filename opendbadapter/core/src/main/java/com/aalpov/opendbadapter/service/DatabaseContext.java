package com.aalpov.opendbadapter.service;

import com.aalpov.opendbadapter.Row;
import com.aalpov.opendbadapter.configuration.DatabaseProperties;
import com.aalpov.opendbadapter.table.ClickhouseTable;

import java.util.Collection;
import java.util.List;

public interface DatabaseContext {

    void insert(Row row, ClickhouseTable table);

    void batchInsert(List<Row> rows, ClickhouseTable table);

    void executeQuery(String query);

    void register(Collection<ClickhouseTable> tables);

    void initialize(DatabaseProperties properties);

    ClickhouseTable getMirror(Class<?> clazz);
}
