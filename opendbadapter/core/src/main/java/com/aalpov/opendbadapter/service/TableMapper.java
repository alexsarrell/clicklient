package com.aalpov.opendbadapter.service;

import com.aalpov.opendbadapter.keys.Engine;
import com.aalpov.opendbadapter.keys.Order;
import com.aalpov.opendbadapter.keys.Partition;
import com.aalpov.opendbadapter.keys.PrimaryKey;
import com.aalpov.opendbadapter.table.Table;
import com.aalpov.opendbadapter.table.TableName;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinition;

public interface TableMapper<T extends Table> {

  Collection<T> mapTables(Set<BeanDefinition> definitions);

    T buildTable(
            TableName name, Field[] fields, Order order, PrimaryKey key, Engine engine, Partition partition, Class<?> mirror);
}
