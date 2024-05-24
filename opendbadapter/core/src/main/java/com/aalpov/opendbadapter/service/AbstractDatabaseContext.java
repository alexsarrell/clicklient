package com.aalpov.opendbadapter.service;

import com.aalpov.opendbadapter.Row;
import com.aalpov.opendbadapter.configuration.DatabaseProperties;
import com.aalpov.opendbadapter.table.ClickhouseTable;
import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

@FunctionalInterface
interface ConnectionConsumer {
    void accept(Connection connection) throws SQLException;
}

public abstract class AbstractDatabaseContext implements DatabaseContext {

    protected Set<ClickhouseTable> tables;

    protected boolean initialized = false;

    DatabaseProperties properties;

    @Override
    public void insert(Row row, ClickhouseTable table) {
        String query = insertQuery(row, table);

        executeQuery(query);
    }

    @Override
    public void batchInsert(List<Row> rows, ClickhouseTable table) {
        String query = batchInsertQuery(rows, table);

        executeQuery(query);
    }

    @Override
    public void executeQuery(String query) {
        System.out.println("Executing query: " + query);

        withConnection(connection -> {
            Statement statement = connection.createStatement();
            statement.executeQuery(query);
            System.out.println("Query " + query + " is successfully executed");
        });
    }

    @Override
    public ClickhouseTable getMirror(Class<?> clazz) {
        var mirror = tables.stream().filter(elem -> elem.getMirror() == clazz).findFirst();
        if (mirror.isPresent()) {
            return mirror.get();
        } else {
            throw new NoSuchElementException(MessageFormat.format("Couldn't find mirror for Class {0}", clazz));
        }
    }

    @Override
    public void register(Collection<ClickhouseTable> tables) {
        if (this.tables == null) {
            this.tables = new HashSet<>();
            this.tables.addAll(tables);
        } else {
            throw new IllegalStateException("Tables are already registered");
        }
    }

    abstract String createTable(ClickhouseTable table);

    @Override
    public void initialize(DatabaseProperties properties) {
        if (!initialized) {
            this.properties = properties;
            init();
            initialized = true;
        } else {
            throw new IllegalStateException("DatabaseContext is already initialized");
        }
    }

    abstract String insertQuery(Row row, ClickhouseTable table);

    abstract String batchInsertQuery(List<Row> rows, ClickhouseTable table);

    protected final void init() {
        withConnection(connection -> {
            for (ClickhouseTable table : tables) {
                String query = createTable(table);
                System.out.println("Execute statement: " + query);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    System.out.println("Result is: " + resultSet);
                }
            }
        });
    }

    protected void withConnection(ConnectionConsumer consumer) {
        try (Connection connection = DriverManager.getConnection(
                properties.getUrl(),
                properties.getUser(),
                properties.getPassword())
        ) {
            System.out.println("Соединение с ClickHouse установлено!");

            consumer.accept(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при подключении к ClickHouse: " + e.getMessage());
        }
    }
}