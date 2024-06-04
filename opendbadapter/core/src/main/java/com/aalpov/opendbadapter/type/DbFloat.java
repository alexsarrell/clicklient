package com.aalpov.opendbadapter.type;

public abstract class DbFloat extends DbNumericType<Float> {

  public DbFloat() {
    super(Float.TYPE, 0f);
  }

  public DbFloat(Float defaultValue) {
    super(Float.TYPE, defaultValue);
  }

  @Override
  public String toSqlName() {
    return super.toSqlName();
  }
}
