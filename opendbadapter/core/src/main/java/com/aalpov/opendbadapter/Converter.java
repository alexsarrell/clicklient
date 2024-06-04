package com.aalpov.opendbadapter;

import com.aalpov.opendbadapter.type.DbType;

interface Converter<T, C extends DbType<?>> {

  C convert(T value);
}
