package com.aalpov.opendbadapter.service;

public interface Converter<Type, Target> {
    Target convert(Type value);
}
