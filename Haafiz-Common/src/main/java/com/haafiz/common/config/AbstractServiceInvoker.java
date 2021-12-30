package com.haafiz.common.config;

/**
 * @author Anthony
 * @create 2021/12/16
 * @desc service call invoker
 */
public class AbstractServiceInvoker implements ServiceInvoker {
	
	protected String invokerPath;
	
	protected String ruleId;
	
	protected int timeout = 5000;

	@Override
	public String getInvokerPath() {
		return invokerPath;
	}

	@Override
	public void getInvokerPath(String invokerPath) {
		this.invokerPath = invokerPath;
	}

	@Override
	public String getRuleId() {
		return ruleId;
	}

	@Override
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;		
	}

	@Override
	public int getTimeout() {
		return timeout;
	}

	@Override
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
