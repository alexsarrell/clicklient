package com.aalpov.opendbadapter.type;

public class DbInt8 extends DbNumericType<Byte> {

    public DbInt8() {
        super();
        this.javaType = Byte.TYPE;
    }
}
