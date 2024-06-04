package com.aalpov.opendbadapter.table;

public abstract class AbstractTable {

  public abstract String getName();

  public abstract Class<?> getMirror();

  public abstract String columns();
}
