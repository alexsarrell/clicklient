package com.aalpov.opendbadapter.type;

public abstract class DbType<T> {

  protected T defaultValue = null;

  protected Class<T> javaType;

  public DbType(Class<T> javaType) {
    this.javaType = javaType;
  }

  public DbType(Class<T> javaType, T defaultValue) {
    this.defaultValue = defaultValue;
    this.javaType = javaType;
  }

  public Class<T> getJavaType() {
    return javaType;
  }

  public T getDefaultValue() {
    return defaultValue;
  }

  public String toSqlName() {
    return this.getClass().getSimpleName().split("Db")[1];
  }

  @Override
  public String toString() {
    return javaType.toString();
  }
}
