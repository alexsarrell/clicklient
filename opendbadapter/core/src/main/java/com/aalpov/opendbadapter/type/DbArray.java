package com.aalpov.opendbadapter.type;

import java.util.Collection;

public class DbArray<T extends DbType<?>> extends DbType<Collection<T>> {

    public DbArray() {
        super();
    }

    public DbType<T> genericType;

    public DbArray(DbType<T> genericType) {
        this.genericType = genericType;
    }

    @Override
    public String toSqlName() {
        return "array(" + genericType.toSqlName() + ")";
    }
}
