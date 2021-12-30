package com.haafiz.common.config;



/**
 * @author Anthony
 * @create 2021/12/16
 * @desc service interface description
 */
public interface ServiceInvoker {

	String getInvokerPath();
	
	void getInvokerPath(String invokerPath);
	

	String getRuleId();
	
	void setRuleId(String ruleId);

	int getTimeout();
	
	void setTimeout(int timeout);
	
}
