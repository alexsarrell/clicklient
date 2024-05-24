package com.aalpov.opendbadapter.table;

import com.aalpov.opendbadapter.Column;
import com.aalpov.opendbadapter.engine.Engine;
import com.aalpov.opendbadapter.keys.Order;

import java.util.List;
import java.util.stream.Collectors;

public class ClickhouseTable {

    final Engine engine;

    final Order order;

    final String name;

    List<Column> columns;

    Class<?> mirror;

    public String getName() {
        return name;
    }

    public Class<?> getMirror() {
        return mirror;
    }

    public String columns() {
        var names = columns.stream().map(Column::getName).toList();
        return "(" + String.join(",", names) + ")";
    }

    public ClickhouseTable(String name, List<Column> columns, Engine engine, Order order, Class<?> mirror) {
        this.name = name;
        this.engine = engine;
        this.columns = columns;
        this.order = order;
        this.mirror = mirror;
    }

    public String toSqlString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(name).append("(");
        for (int i = 0; i < columns.size(); i++) {
            sb.append(columns.get(i).getName()).append(" ").append(columns.get(i).getType().toSqlName());
            if (columns.size() - 1 > i) {
                sb.append(',');
            }
        }
        sb.append(')');
        sb.append("\n ENGINE = ").append(engine.toSqlString());
        sb.append("\n ORDER BY ").append(order.toString());
        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("\nTable: ").append(name);
        sb.append(" {\n");
        for (Column column : columns) {
            sb
                    .append("    ")
                    .append(column.toString())
                    .append(";\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
