package com.haafiz.common.config;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Anthony
 * @create 2021/12/16
 * @desc dynamic service cache management
 */
public class DynamicConfigManager {
	
	//	one uniqueId with one ServiceDefinition
	private ConcurrentHashMap<String /* uniqueId */ , ServiceDefinition>  serviceDefinitionMap = new ConcurrentHashMap<>();
	
	//	one uniqueId with multi ServiceInstance
	private ConcurrentHashMap<String /* uniqueId */ , Set<ServiceInstance>>  serviceInstanceMap = new ConcurrentHashMap<>();

	//	rule collection
	private ConcurrentHashMap<String /* ruleId */ , Rule>  ruleMap = new ConcurrentHashMap<>();
	
	private DynamicConfigManager() {
	}
	
	private static class SingletonHolder {
		private static final DynamicConfigManager INSTANCE = new DynamicConfigManager();
	}

	
	public static DynamicConfigManager getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public void putServiceDefinition(String uniqueId, ServiceDefinition serviceDefinition) {
		serviceDefinitionMap.put(uniqueId, serviceDefinition);;
	}
	
	public ServiceDefinition getServiceDefinition(String uniqueId) {
		return serviceDefinitionMap.get(uniqueId);
	}
	
	public void removeServiceDefinition(String uniqueId) {
		serviceDefinitionMap.remove(uniqueId);
	}
	
	public ConcurrentHashMap<String, ServiceDefinition> getServiceDefinitionMap() {
		return serviceDefinitionMap;
	}
	
	/***************** 	对服务实例缓存进行操作的系列方法 	***************/

	public void addServiceInstance(String uniqueId, ServiceInstance serviceInstance) {
		Set<ServiceInstance> set = serviceInstanceMap.get(uniqueId);
		set.add(serviceInstance);
	}
	
	public void updateServiceInstance(String uniqueId, ServiceInstance serviceInstance) {
		Set<ServiceInstance> set = serviceInstanceMap.get(uniqueId);
		Iterator<ServiceInstance> it = set.iterator();
		while(it.hasNext()) {
			ServiceInstance is = it.next();
			if(is.getServiceInstanceId().equals(serviceInstance.getServiceInstanceId())) {
				it.remove();
				break;
			}
		}
		set.add(serviceInstance);
	}
	
	public void removeServiceInstance(String uniqueId, String serviceInstanceId) {
		Set<ServiceInstance> set = serviceInstanceMap.get(uniqueId);
		Iterator<ServiceInstance> it = set.iterator();
		while(it.hasNext()) {
			ServiceInstance is = it.next();
			if(is.getServiceInstanceId().equals(serviceInstanceId)) {
				it.remove();
				break;
			}
		}
	}
	
	public void removeServiceInstancesByUniqueId(String uniqueId) {
		serviceInstanceMap.remove(uniqueId);
	}
	
		
	/***************** 	对规则缓存进行操作的系列方法 	***************/
	
	public void putRule(String ruleId, Rule rule) {
		ruleMap.put(ruleId, rule);
	}
	
	public Rule getRule(String ruleId) {
		return ruleMap.get(ruleId);
	}
	
	public void removeRule(String ruleId) {
		ruleMap.remove(ruleId);
	}
	
	public ConcurrentHashMap<String, Rule> getRuleMap() {
		return ruleMap;
	}
	

}
