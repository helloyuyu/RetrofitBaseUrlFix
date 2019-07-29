package com.helloyuyu.baseurlfix.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置baseUrl注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface BaseUrl {
    String value();
}
