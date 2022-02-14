package com.haafiz.client;

import java.lang.annotation.*;

/**
 * @author Anthony
 * @create 2022/1/15
 * @desc required on method to be scanned
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HaafizInvoker {

    /**
     * request path
     *
     * @return
     */
    String path();
}
