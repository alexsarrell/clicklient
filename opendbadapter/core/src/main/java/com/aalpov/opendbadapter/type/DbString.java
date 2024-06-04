package com.aalpov.opendbadapter.type;

public class DbString extends DbType<String> {

  public DbString() {
    super(String.class, "");
  }

  @Override
  public String toString() {
    return "String";
  }
}
