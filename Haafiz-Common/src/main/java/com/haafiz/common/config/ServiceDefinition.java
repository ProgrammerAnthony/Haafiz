package com.haafiz.common.config;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * @author Anthony
 * @create 2021/12/16
 * @desc ServiceDefinition is a service registry info
 */
@Data
public class ServiceDefinition implements Serializable {

	private static final long serialVersionUID = -8263365765897285189L;
	
	/**
	 * 	唯一的服务ID: serviceId:version
	 */
	private String uniqueId;
	
	/**
	 * 	服务唯一id
	 */
	private String serviceId;
	
	/**
	 * 	服务的版本号
	 */
	private String version;
	
	/**
	 * 	服务的具体协议：http(mvc http) dubbo ..
	 */
	private String protocol;
	
	/**
	 * 	路径匹配规则：访问真实ANT表达式：定义具体的服务路径的匹配规则
	 */
	private String patternPath;
	
	/**
	 * 	环境名称
	 */
	private String envType;

	/**
	 * 	服务启用禁用
	 */
	private boolean enable = true;
	
	/**
	 * 	服务列表信息：
	 */
	private Map<String /* invokerPath */, ServiceInvoker> invokerMap;


	public ServiceDefinition() {
		super();
	}
	
	public ServiceDefinition(String uniqueId, String serviceId, String version, String protocol, String patternPath,
			String envType, boolean enable, Map<String, ServiceInvoker> invokerMap) {
		super();
		this.uniqueId = uniqueId;
		this.serviceId = serviceId;
		this.version = version;
		this.protocol = protocol;
		this.patternPath = patternPath;
		this.envType = envType;
		this.enable = enable;
		this.invokerMap = invokerMap;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(this == null || getClass() != o.getClass()) return false;
		ServiceDefinition serviceDefinition = (ServiceDefinition)o;
		return Objects.equals(uniqueId, serviceDefinition.uniqueId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(uniqueId);
	}

}
