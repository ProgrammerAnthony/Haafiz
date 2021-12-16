package com.haafiz.common.exception;


/**
 * @author Anthony
 * @create 2021/12/16
 * @desc
 */
public class HaafizBasicException extends RuntimeException {

    private static final long serialVersionUID = -5658789202563433456L;

    public HaafizBasicException() {
    }

    protected ResponseCode code;

    public HaafizBasicException(String message, ResponseCode code) {
        super(message);
        this.code = code;
    }

    public HaafizBasicException(String message, Throwable cause, ResponseCode code) {
        super(message, cause);
        this.code = code;
    }

    public HaafizBasicException(ResponseCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public HaafizBasicException(String message, Throwable cause,
                              boolean enableSuppression, boolean writableStackTrace, ResponseCode code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public ResponseCode getCode() {
        return code;
    }

}

