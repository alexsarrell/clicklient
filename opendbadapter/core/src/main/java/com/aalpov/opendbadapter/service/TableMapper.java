package com.aalpov.opendbadapter.service;

import com.aalpov.opendbadapter.keys.Order;
import com.aalpov.opendbadapter.keys.Partition;
import com.aalpov.opendbadapter.table.AbstractTable;
import com.aalpov.opendbadapter.table.ClickhouseTable;
import com.aalpov.opendbadapter.table.TableName;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinition;

public interface TableMapper<T extends AbstractTable> {

  Collection<T> mapTables(Set<BeanDefinition> definitions);

    T buildTable(
            TableName name, Field[] fields, Order order, Partition partition, Class<?> mirror);
}
