package com.aalpov.opendbadapter.type;

public abstract class DbNumericType<T extends Number> extends DbType<T> {

  public DbNumericType(Class<T> javaType) {
    super(javaType);
  }

  public DbNumericType(Class<T> javaType, T defaultValue) {
    super(javaType, defaultValue);
  }
}
