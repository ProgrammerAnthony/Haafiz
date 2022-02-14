package com.haafiz.client.autoconfig;

import com.haafiz.client.registry.DubboClientRegistryManager;
import com.haafiz.client.registry.SpringMVCClientRegistryManager;
import org.apache.dubbo.config.spring.ServiceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Servlet;

/**
 * @author Anthony
 * @create 2022/1/15
 * @desc spring boot Auto Configuration
 */
@Configuration
@EnableConfigurationProperties(HaafizProperties.class)
@ConditionalOnProperty(prefix = HaafizProperties.HAAFIZ_PREFIX, name = {"registryAddress", "namespace"})
public class HaafizClientAutoConfiguration {

    @Autowired
    private HaafizProperties haafizProperties;

    @Bean
    @ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class})
    @ConditionalOnMissingBean(SpringMVCClientRegistryManager.class)
    public SpringMVCClientRegistryManager springMvcClientRegistryManager() throws Exception {
        return new SpringMVCClientRegistryManager(haafizProperties);
    }

    @Bean
    @ConditionalOnClass({ServiceBean.class})
    @ConditionalOnMissingBean(DubboClientRegistryManager.class)
    public DubboClientRegistryManager dubboClientRegistryManager() throws Exception {
        return new DubboClientRegistryManager(haafizProperties);
    }

}
