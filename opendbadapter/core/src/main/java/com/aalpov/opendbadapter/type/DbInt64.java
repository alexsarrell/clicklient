package com.aalpov.opendbadapter.type;

public class DbInt64 extends DbNumericType<Long> {

    public DbInt64() {
        super();
        this.javaType = Long.TYPE;
    }
}
