package com.haafiz.common.config;


/**
 * @author Anthony
 * @create 2021/12/16
 * @desc service registry under Dubbo protocol
 */
public class DubboServiceInvoker extends AbstractServiceInvoker {
	
	//	service registry address
	private String registerAddress;
	
	//	interface class name
	private String interfaceClass;
	
	//	method name
	private String methodName;
	
	//	param collections
	private String[] parameterTypes;
	
	//	Dubbo service vision
	private String version;

	public String getRegisterAddress() {
		return registerAddress;
	}

	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}

	public String getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(String interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(String[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
