package com.haafiz.common.constants;


public interface HaafizProtocol {
	
	String HTTP = "http";
	
	String DUBBO = "dubbo";
	
	static boolean isHttp(String protocol) {
		return HTTP.equals(protocol);
	}
	
	static boolean isDubbo(String protocol) {
		return DUBBO.equals(protocol);
	}
	
}
