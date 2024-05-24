package com.aalpov.opendbadapter.type;

import java.math.BigInteger;

public class DbUInt128 extends DbNumericType<BigInteger> {

    public DbUInt128() {
        super();
        this.javaType = BigInteger.class;
    }
}
