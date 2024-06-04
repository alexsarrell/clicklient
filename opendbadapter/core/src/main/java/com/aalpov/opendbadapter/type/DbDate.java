package com.aalpov.opendbadapter.type;

import java.time.Instant;
import java.util.Date;

public class DbDate extends DbType<Date> {

  public DbDate() {
    super(Date.class, Date.from(Instant.ofEpochMilli(0)));
  }

  @Override
  public String toSqlName() {
    return super.toSqlName();
  }

  @Override
  public String toString() {
    return super.toString();
  }
}
