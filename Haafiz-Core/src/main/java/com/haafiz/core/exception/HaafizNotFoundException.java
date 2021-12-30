package com.haafiz.core.exception;


import com.haafiz.common.exception.HaafizBasicException;
import com.haafiz.common.exception.ResponseCode;


public class HaafizNotFoundException extends HaafizBasicException {

	private static final long serialVersionUID = -5534700534739261761L;

	public HaafizNotFoundException(ResponseCode code) {
		super(code.getMessage(), code);
	}
	
	public HaafizNotFoundException(Throwable cause, ResponseCode code) {
		super(code.getMessage(), cause, code);
	}
	
}
