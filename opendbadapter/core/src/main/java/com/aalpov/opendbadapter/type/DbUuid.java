package com.aalpov.opendbadapter.type;

import java.util.UUID;

public class DbUuid extends DbType<UUID> {

  public DbUuid() {
    super(UUID.class);
  }
}
