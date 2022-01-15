package com.haafiz.common.config;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * @author Anthony
 * @create 2021/12/16
 * @desc {@link ServiceDefinition} is a service registry info
 */
@Data
public class ServiceDefinition implements Serializable {

	private static final long serialVersionUID = -8263365765897285189L;
	
	/**
	 *  serviceId:version
	 */
	private String uniqueId;
	
	/**
	 * 	the only service id
	 */
	private String serviceId;
	
	/**
	 * 	service version
	 */
	private String version;
	
	/**
	 * 	protocol：http(mvc http) dubbo ..
	 */
	private String protocol;
	
	/**
	 * ANT	path pattern: define specific ANT pattern of path
	 */
	private String patternPath;
	
	/**
	 * 	environment type
	 */
	private String envType;

	/**
	 * 	enable service
	 */
	private boolean enable = true;
	
	/**
	 * 	service invoke map
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
