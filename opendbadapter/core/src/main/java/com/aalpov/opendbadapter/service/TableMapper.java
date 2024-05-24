package com.aalpov.opendbadapter.service;

import com.aalpov.opendbadapter.keys.Order;
import com.aalpov.opendbadapter.model.TableName;
import com.aalpov.opendbadapter.table.ClickhouseTable;

import java.lang.reflect.Field;

public interface TableMapper {

    ClickhouseTable register(TableName name, Field[] fields, Order order, Class<?> mirror);
}
