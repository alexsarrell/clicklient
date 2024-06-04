package com.aalpov.opendbadapter.service;

import com.aalpov.opendbadapter.table.AbstractTable;

public interface TablesRegistrar<T extends AbstractTable> {

  void scanAndRegister(String[] basePackages);

  DatabaseContext<T> getContext();
}
