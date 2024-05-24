package com.aalpov.opendbadapter.type;

public class DbFloat64 extends DbNumericType<Double> {

    public DbFloat64() {
        super();
        this.javaType = Double.TYPE;
    }
}
