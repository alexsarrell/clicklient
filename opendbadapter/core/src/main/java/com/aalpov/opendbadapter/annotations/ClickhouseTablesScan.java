package com.aalpov.opendbadapter.annotations;

import com.aalpov.opendbadapter.configuration.ClickhouseConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ClickhouseConfiguration.class)
public @interface ClickhouseTablesScan {

    String[] basePackages() default { "" };
}
