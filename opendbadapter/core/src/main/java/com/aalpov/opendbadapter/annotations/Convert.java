package com.aalpov.opendbadapter.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({METHOD, FIELD, TYPE})
@Retention(RUNTIME)
@SuppressWarnings("rawtypes")
public @interface Convert {

  Class converter();

  String attributeName() default "";
}
