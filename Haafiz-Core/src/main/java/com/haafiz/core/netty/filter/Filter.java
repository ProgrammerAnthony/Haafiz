package com.haafiz.core.netty.filter;

import java.lang.annotation.*;

/**
 * @author Anthony
 * @create 2022/1/9
 * @desc annotation for filter
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Filter {
    String id();

    String name() default "";

    ProcessorFilterType value();

    int order() default 0;
}
