package com.aalpov.opendbadapter.annotations;

import com.aalpov.opendbadapter.configuration.ClickhouseConfiguration;
import java.lang.annotation.*;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ClickhouseConfiguration.class)
public @interface ClickhouseTablesScan {

  String[] basePackages() default {""};
}
