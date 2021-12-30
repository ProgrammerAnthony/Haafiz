package com.haafiz.core.exception;

import com.haafiz.common.exception.HaafizBasicException;
import com.haafiz.common.exception.ResponseCode;


public class RapidPathNoMatchedException extends HaafizBasicException {

	private static final long serialVersionUID = -6695383751311763169L;

	
	public RapidPathNoMatchedException() {
		this(ResponseCode.PATH_NO_MATCHED);
	}
	
	public RapidPathNoMatchedException(ResponseCode code) {
		super(code.getMessage(), code);
	}
	
	public RapidPathNoMatchedException(Throwable cause, ResponseCode code) {
		super(code.getMessage(), cause, code);
	}
}
