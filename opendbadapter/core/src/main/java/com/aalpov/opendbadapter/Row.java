package com.aalpov.opendbadapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Row {

    Map<String, Object> row;

    public Row(Map<String, Object> row) {
        this.row = row;
    }

    public String keysToSqlString() {
        var keys = row.keySet().stream().toList();
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(String.join(",", keys));
        builder.append(")");
        builder.append(" VALUES ");

        return builder.toString();
    }

    public String valuesToSqlString() {
        List<String> keys = row.keySet().stream().toList();
        StringBuilder builder = new StringBuilder();
        List<String> values = new ArrayList<>();

        keys.forEach(key -> {
            var value = row.get(key);
            if (value instanceof String) {
                values.add("'" + value + "'");
            } else {
                values.add(value.toString());
            }
        });

        builder.append("(");
        builder.append(String.join(",", values));
        builder.append(")");
        return builder.toString();
    }
}
