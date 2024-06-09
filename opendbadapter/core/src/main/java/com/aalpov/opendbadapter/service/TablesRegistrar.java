package com.aalpov.opendbadapter.service;

import com.aalpov.opendbadapter.table.Table;

public interface TablesRegistrar<T extends Table> {

  void scanAndRegister(String[] basePackages);

  DatabaseContext<T> getContext();
}
