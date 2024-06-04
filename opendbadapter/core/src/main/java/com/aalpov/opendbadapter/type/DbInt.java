package com.aalpov.opendbadapter.type;

public abstract class DbInt extends DbNumericType<Integer> {

  public DbInt() {
    super(Integer.TYPE);
  }

  public DbInt(Class<Integer> javaType) {
    super(javaType, 1);
  }

  public DbInt(Class<Integer> javaType, Integer defaultValue) {
    super(javaType, defaultValue);
  }
}
