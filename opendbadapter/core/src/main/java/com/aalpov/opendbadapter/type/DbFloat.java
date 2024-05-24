package com.aalpov.opendbadapter.type;

public abstract class DbFloat extends DbNumericType<Float> {

    public DbFloat() {
        this.defaultValue = 0f;
        this.javaType = Float.TYPE;
    }

    @Override
    public String toSqlName() {
        return super.toSqlName();
    }
}
