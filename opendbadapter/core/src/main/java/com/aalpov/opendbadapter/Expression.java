package com.aalpov.opendbadapter;

import java.util.Arrays;
import java.util.List;

public class Expression {
    String expression;

    public Expression(String expression) {
        this.expression = expression;
    }

    public Expression(String[] columns) {
        this.expression = "(" + String.join(",", columns) + ")";
    }

    @Override
    public String toString() {
        return expression;
    }
}
