package com.aalpov.opendbadapter.service;

import com.aalpov.opendbadapter.type.DbType;

import java.lang.reflect.Field;

public interface Converter<Source, Target, Type extends DbType<Target>> {

    Target convert(Source value);

    Type convertType(Field field);
}
