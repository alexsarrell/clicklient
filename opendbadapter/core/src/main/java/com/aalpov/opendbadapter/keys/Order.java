package com.aalpov.opendbadapter.keys;

import com.aalpov.opendbadapter.Expression;

public class Order {
    Expression expression;

    public Order(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return expression.toString();
    }
}
