package com.aalpov.opendbadapter.type;

public abstract class DbType<T> {

    T defaultValue = null;

    public Class<T> javaType;

    public String toSqlName() {
        return this.getClass()
                .getSimpleName()
                .split("Db")[1];
    }

    @Override
    public String toString() {
        return javaType.toString();
    }
}
