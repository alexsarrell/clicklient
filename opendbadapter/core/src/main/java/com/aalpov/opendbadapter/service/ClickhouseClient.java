package com.aalpov.opendbadapter.service;

import java.util.Collection;

public interface ClickhouseClient extends DatabaseClient {

    void insert(Object row);

    void batchInsert(Collection<Object> rows);
}
