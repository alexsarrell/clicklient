package com.aalpov.opendbadapter.type;

import java.util.Collection;

public class DbArray<T> extends DbType<Collection<DbType<T>>> {

    @SuppressWarnings("unchecked")
    public DbArray() {
        super((Class<Collection<DbType<T>>>)(Class<?>)Collection.class);
    }

    public DbType<T> genericType;

    @SuppressWarnings("unchecked")
    public DbArray(DbType<T> genericType) {
        super((Class<Collection<DbType<T>>>)(Class<?>)Collection.class);
        this.genericType = genericType;
    }

    @Override
    public String toSqlName() {
        return "array(" + genericType.toSqlName() + ")";
    }
}
