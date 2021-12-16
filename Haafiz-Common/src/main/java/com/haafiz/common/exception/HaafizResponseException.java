package com.haafiz.common.exception;


/**
 * @author Anthony
 * @create 2021/12/16
 * @desc
 */
public class HaafizResponseException extends HaafizBasicException {

    private static final long serialVersionUID = -5658789202509039759L;

    public HaafizResponseException() {
        this(ResponseCode.INTERNAL_ERROR);
    }

    public HaafizResponseException(ResponseCode code) {
        super(code.getMessage(), code);
    }

    public HaafizResponseException(Throwable cause, ResponseCode code) {
        super(code.getMessage(), cause, code);
        this.code = code;
    }

}