package com.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解用来设置表明
 */
@Retention(RetentionPolicy.RUNTIME) //运行时保留注解信息
@Target(ElementType.TYPE)  //注解作用与类上
public @interface ORMTable {

    public String name() default "";
}
