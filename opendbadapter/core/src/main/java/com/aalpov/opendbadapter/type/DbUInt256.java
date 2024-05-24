package com.aalpov.opendbadapter.type;

import java.math.BigInteger;

public class DbUInt256 extends DbNumericType<BigInteger> {

    public DbUInt256() {
        super();
        this.javaType = BigInteger.class;
    }
}
