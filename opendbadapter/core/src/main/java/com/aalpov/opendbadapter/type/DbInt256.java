package com.aalpov.opendbadapter.type;

import java.math.BigInteger;

public class DbInt256 extends DbNumericType<BigInteger> {

    public DbInt256() {
        super();
        this.javaType = BigInteger.class;
    }
}
