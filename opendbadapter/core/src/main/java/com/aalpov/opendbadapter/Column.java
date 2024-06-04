package com.aalpov.opendbadapter;

import com.aalpov.opendbadapter.type.DbType;
import java.lang.reflect.Field;
import org.jetbrains.annotations.NotNull;

public class Column<C extends DbType<T>, T> {
  String name;
  DbType<T> type;

  public Column(@NotNull Field field, @NotNull C type) {
    name = field.getName();
    this.type = type;
  }

  public Column(@NotNull String name, @NotNull C type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public DbType<T> getType() {
    return type;
  }

  @Override
  public String toString() {
    return name + ": " + type.toSqlName();
  }
}
