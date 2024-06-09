package com.aalpov.opendbadapter.keys;

import com.aalpov.opendbadapter.Expression;

public class PrimaryKey {

    Expression expression;

    public PrimaryKey(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return expression.toString();
    }
}
