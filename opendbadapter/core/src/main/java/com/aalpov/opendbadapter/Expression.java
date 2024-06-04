package com.aalpov.opendbadapter;

import java.util.Objects;

public class Expression {
  String expression;

  public Expression(String expression) {
    this.expression = expression;
  }

  public Expression(String expression, String[] columns) {
    if (!Objects.equals(expression, "") && columns.length != 0) {
      this.expression = "(" + expression + "," + String.join(",", columns) + ")";
    } else if (!Objects.equals(expression, "")) {
      this.expression = "(" + expression + ")";
    } else {
      this.expression = "(" + String.join(",", columns) + ")";
    }
  }

  public Expression(String[] columns) {
    this.expression = "(" + String.join(",", columns) + ")";
  }

  @Override
  public String toString() {
    return expression;
  }
}
