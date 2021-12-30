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
	
	//	规则ID 全局唯一
	private String id;
	
	//	规则名称
	private String name;
	
	//	规则对应的协议
	private String protocol;
	
	//	规则排序，用于以后万一有需求做一个路径绑定多种规则，但是只能最终执行一个规则（按照该属性做优先级判断）
	private Integer order;
	
	//	规则集合定义
	private Set<FilterConfig> filterConifgs = new HashSet<>();
	
	/**
	 * <B>方法名称：</B>addFilterConfig<BR>
	 * <B>概要说明：</B>向规则里面添加指定的过滤器<BR>
	 * @author JiFeng
	 * @since 2021年12月9日 下午2:21:07
	 * @param filterConfig
	 * @return
	 */
	public boolean addFilterConfig(FilterConfig filterConfig) {
		return filterConifgs.add(filterConfig);
	}
	
	/**
	 * <B>方法名称：</B>getFilterConfig<BR>
	 * <B>概要说明：</B>通过一个指定的filterId 获取getFilterConfig<BR>
	 * @author JiFeng
	 * @since 2021年12月9日 下午2:22:57
	 * @param id
	 * @return Rule.FilterConfig
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
	 * <B>方法名称：</B>hashId<BR>
	 * <B>概要说明：</B>根据传入的filterId 判断当前Rule中是否存在<BR>
	 * @author JiFeng
	 * @since 2021年12月9日 下午2:24:27
	 * @param id
	 * @return boolean
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
	 * <B>主类名称：</B>FilterConfig<BR>
	 * <B>概要说明：</B>过滤器的配置类<BR>
	 * @author JiFeng
	 * @since 2021年12月9日 下午2:10:13
	 */
	public static class FilterConfig {
		
		//	过滤器的唯一ID
		private String id;
		
		//	过滤器的配置信息描述：json string 
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
