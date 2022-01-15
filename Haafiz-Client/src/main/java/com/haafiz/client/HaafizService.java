package com.haafiz.client;


import java.lang.annotation.*;

/**
 * @author Anthony
 * @create 2022/1/15
 * @desc service annotation
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HaafizService {
    /**
     * unique service id
     *
     * @return
     */
    String serviceId();

    /**
     * service version,which will be overridden by Dubbo
     *
     * @return
     */
    String version() default "1.0.0";

    /**
     * for ANT path config
     * @return
     */
    String patternPath();

    /**
     * what kind of protocol
     * @return
     */
    HaafizProtocol protocol();

}
