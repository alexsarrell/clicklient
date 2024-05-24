package com.aalpov.opendbadapter.type;

import java.util.Map;

public class DbMap<K extends DbType<?>, V extends DbType<?>> extends DbType<Map<K, V>> {

    public DbMap() {
        super();
    }
}
