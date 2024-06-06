package com.aalpov.opendbadapter.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

  String name();

  String[] primaryKey() default "";

  String[] orderBy() default "";

  String[] partitionBy() default "";
}
