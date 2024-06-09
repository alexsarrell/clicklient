package com.aalpov.opendbadapter.service;

import com.aalpov.opendbadapter.Row;
import com.aalpov.opendbadapter.configuration.DatabaseProperties;
import com.aalpov.opendbadapter.table.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

@FunctionalInterface
interface ConnectionConsumer {

  void accept(Connection connection) throws SQLException;
}

public abstract class AbstractDatabaseContext<T extends Table>
    implements DatabaseContext<T> {

  private static final Logger logger = LoggerFactory.getLogger(AbstractDatabaseContext.class);

  protected Set<T> tables;

  protected boolean initialized = false;

  DatabaseProperties properties;

  private DataSource dataSource = null;

  public AbstractDatabaseContext(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public AbstractDatabaseContext() {
  }

  @Override
  public void insert(Row row, T table) {
    String query = insertQuery(row, table);

    executeQuery(query);
  }

  @Override
  public void batchInsert(List<Row> rows, T table) {
    String query = batchInsertQuery(rows, table);

    executeQuery(query);
  }

  @Override
  public void executeQuery(String query) {
    logger.info("Executing query: " + query);

    withConnection(
        connection -> {
          Statement statement = connection.createStatement();
          statement.executeQuery(query);
          logger.info("Query " + query + " is successfully executed");
        });
  }

  @Override
  public T getMirror(Class<?> clazz) {
    var mirror = tables.stream().filter(elem -> elem.getMirror() == clazz).findFirst();
    if (mirror.isPresent()) {
      return mirror.get();
    } else {
      throw new NoSuchElementException(
          MessageFormat.format("Couldn't find mirror for Class {0}", clazz));
    }
  }

  @Override
  public void register(Collection<T> tables) {
    if (this.tables == null) {
      this.tables = new HashSet<>();
      this.tables.addAll(tables);
    } else {
      throw new IllegalStateException("Tables are already registered");
    }
  }

  abstract String createTable(T table);

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

  abstract String insertQuery(Row row, T table);

  abstract String batchInsertQuery(List<Row> rows, T table);

  protected final void init() {
    withConnection(
        connection -> {
          for (T table : tables) {
            String query = createTable(table);
            logger.info("Execute statement: " + query);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
              logger.info("Result is: " + resultSet);
            }
          }
        });
  }

  private Connection getConnection() throws SQLException {
    if (dataSource != null) {
      return dataSource.getConnection(properties.getUser(), properties.getPassword());
    } else {
      logger.debug("DataSource is not initialized, using DriverManager instead");
      return DriverManager.getConnection(
              properties.getUrl(), properties.getUser(), properties.getPassword());
    }
  }

  protected void withConnection(ConnectionConsumer consumer) {
    try (Connection connection = getConnection()) {
      logger.info("Соединение с базой данных установлено!");

      consumer.accept(connection);
    } catch (SQLException e) {
      logger.error("Ошибка при подключении к базе данных: " + e.getMessage());
    }
  }
}
