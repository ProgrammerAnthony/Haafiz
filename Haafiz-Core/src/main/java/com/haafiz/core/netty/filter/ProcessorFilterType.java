package com.haafiz.core.netty.filter;

/**
 * @author Anthony
 * @create 2022/1/9
 * @desc there are four kinds of filter type
 */
public enum ProcessorFilterType {
    PRE("PRE", "Pre Filter"),

    ROUTE("ROUTE", "Route Filter"),

    ERROR("ERROR", "Error Filter"),

    POST("POST", "Post Filter");

    private final String code;
    private final String message;

    ProcessorFilterType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
