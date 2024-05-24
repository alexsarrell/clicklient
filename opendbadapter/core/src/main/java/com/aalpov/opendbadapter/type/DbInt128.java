package com.aalpov.opendbadapter.type;

import java.math.BigInteger;

public class DbInt128 extends DbNumericType<BigInteger> {

    public DbInt128() {
        super();
        this.javaType = BigInteger.class;
    }
}
