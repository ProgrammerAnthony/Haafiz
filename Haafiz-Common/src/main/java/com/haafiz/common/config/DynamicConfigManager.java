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
	
	//	服务的定义集合：uniqueId代表服务的唯一标识
	private ConcurrentHashMap<String /* uniqueId */ , ServiceDefinition>  serviceDefinitionMap = new ConcurrentHashMap<>();
	
	//	服务的实例集合：uniqueId与一对服务实例对应
	private ConcurrentHashMap<String /* uniqueId */ , Set<ServiceInstance>>  serviceInstanceMap = new ConcurrentHashMap<>();

	//	规则集合
	private ConcurrentHashMap<String /* ruleId */ , Rule>  ruleMap = new ConcurrentHashMap<>();
	
	private DynamicConfigManager() {
	}
	
	private static class SingletonHolder {
		private static final DynamicConfigManager INSTANCE = new DynamicConfigManager();
	}
	
	
	/***************** 	对服务定义缓存进行操作的系列方法 	***************/
	
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
