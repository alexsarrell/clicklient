package com.aalpov.opendbadapter.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({METHOD, FIELD, TYPE})
@Retention(RUNTIME)
public @interface Convert {

  Class converter() default void.class;

  String attributeName() default "";
}
