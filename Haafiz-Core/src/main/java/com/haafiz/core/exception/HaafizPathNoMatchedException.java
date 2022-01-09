package com.haafiz.core.exception;

import com.haafiz.common.exception.HaafizBasicException;
import com.haafiz.common.exception.ResponseCode;


public class HaafizPathNoMatchedException extends HaafizBasicException {

	private static final long serialVersionUID = -6695383751311763169L;

	
	public HaafizPathNoMatchedException() {
		this(ResponseCode.PATH_NO_MATCHED);
	}
	
	public HaafizPathNoMatchedException(ResponseCode code) {
		super(code.getMessage(), code);
	}
	
	public HaafizPathNoMatchedException(Throwable cause, ResponseCode code) {
		super(code.getMessage(), cause, code);
	}
}
