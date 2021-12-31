package com.haafiz.common.config;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * @author Anthony
 * @create 2021/12/16
 * @desc rule
 */
@Data
public class Rule implements Comparable<Rule>, Serializable {

	private static final long serialVersionUID = 2540640682854847548L;
	
	//	unique id of rule
	private String id;
	
	//	rule name
	private String name;
	
	//	protocol for rule
	private String protocol;
	
	//	规则排序，用于以后万一有需求做一个路径绑定多种规则，但是只能最终执行一个规则（按照该属性做优先级判断）
	private Integer order;
	
	//	规则集合定义
	private Set<FilterConfig> filterConifgs = new HashSet<>();
	

	/**
	 * add filter config for rule
	 * @param filterConfig
	 * @return
	 */
	public boolean addFilterConfig(FilterConfig filterConfig) {
		return filterConifgs.add(filterConfig);
	}

	/**
	 * load {@link FilterConfig} by filterId
	 * @param id
	 * @return
	 */
	public FilterConfig getFilterConfig(String id){
		for(FilterConfig filterConfig : filterConifgs) {
			if(filterConfig.getId().equalsIgnoreCase(id)) {
				return filterConfig;
			}
		}
		return null;
	}

	/**
	 * judge exists by filterId
	 * @param id
	 * @return
	 */
	public boolean hashId(String id) {
		for(FilterConfig filterConfig : filterConifgs) {
			if(filterConfig.getId().equalsIgnoreCase(id)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int compareTo(Rule o) {
		int orderCompare = Integer.compare(getOrder(), o.getOrder());
		if(orderCompare == 0) {
			return getId().compareTo(o.getId());
		}
		return orderCompare;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if((o == null) || getClass() != o.getClass()) return false;
		Rule that = (Rule)o;
		return id.equals(that.id);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	

	/**
	 * config for filter
	 */
	public static class FilterConfig {
		
		//	filter unique id
		private String id;
		
		//	filter config desc：json string
		private String config;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getConfig() {
			return config;
		}

		public void setConfig(String config) {
			this.config = config;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if((o == null) || getClass() != o.getClass()) return false;
			FilterConfig that = (FilterConfig)o;
			return id.equals(that.id);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(id);
		}
	}
	
}
