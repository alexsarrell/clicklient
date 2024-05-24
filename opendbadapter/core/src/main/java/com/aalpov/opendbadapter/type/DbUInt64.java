package com.aalpov.opendbadapter.type;

public class DbUInt64 extends DbNumericType<Long> {

    public DbUInt64() {
        super();
        this.javaType = Long.TYPE;
    }
}
