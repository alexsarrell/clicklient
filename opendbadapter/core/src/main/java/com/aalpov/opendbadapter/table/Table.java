package com.aalpov.opendbadapter.table;

public interface Table {

  String getName();

  Class<?> getMirror();

  String columns();
}
