package com.aalpov.opendbadapter.type;

public class DbUInt32 extends DbNumericType<Long> {

    public DbUInt32() {
        super();
        this.javaType = Long.TYPE;
    }
}
