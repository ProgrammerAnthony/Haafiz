package com.haafiz.common.config;


/**
 * @author Anthony
 * @create 2021/12/16
 * @desc service registry under dubbo protocol
 */
public class DubboServiceInvoker extends AbstractServiceInvoker {
	
	//	注册中心地址
	private String registerAddress;
	
	//	接口全类名
	private String interfaceClass;
	
	//	方法名称
	private String methodName;
	
	//	参数名字的集合
	private String[] parameterTypes;
	
	//	dubbo服务的版本号
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
