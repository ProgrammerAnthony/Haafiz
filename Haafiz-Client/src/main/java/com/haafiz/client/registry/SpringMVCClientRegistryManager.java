package com.haafiz.client.registry;

import com.haafiz.client.HaafizAnnotationScanner;
import com.haafiz.client.autoconfig.HaafizProperties;
import com.haafiz.common.config.ServiceDefinition;
import com.haafiz.common.config.ServiceInstance;
import com.haafiz.common.constants.BasicConst;
import com.haafiz.common.constants.HaafizConst;
import com.haafiz.common.util.NetUtils;
import com.haafiz.common.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Anthony
 * @create 2022/1/15
 * @desc
 */
@Slf4j
public class SpringMVCClientRegistryManager extends  AbstractClientRegistryManager  implements ApplicationListener<ApplicationEvent>, ApplicationContextAware, SpringMVCRegistry{
    ApplicationContext applicationContext;
    private static final Set<Object> uniqueBeanSet = new HashSet<>();
    @Autowired
    private ServerProperties serverProperties;

    public SpringMVCClientRegistryManager(HaafizProperties haafizProperties) throws Exception {
        super(haafizProperties);
    }

    @PostConstruct
    private void init() {
        if(!ObjectUtils.allNotNull(serverProperties, serverProperties.getPort())) {
            return;
        }
        whetherStart = true;
    }

    @Override
    public List<ServiceInstance> registerServiceInstance(Map<String, RequestMappingHandlerMapping> allRequestMappings) throws Exception {
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(!whetherStart) {
            return;
        }
        if(event instanceof WebServerInitializedEvent ||
                event instanceof ServletWebServerInitializedEvent) {
            try {
                registerSpringMvc();
            } catch (Exception e) {
                log.error("#SpringMvcClientRegistryManager# registerSpringMvc error", e);
            }
        } else if(event instanceof ApplicationStartedEvent){
            System.out.println("******************************************");
            System.out.println("**        Haafiz SpringMvc Started       **");
            System.out.println("******************************************");
        }
    }

    private void registerSpringMvc() throws Exception{
        Map<String, RequestMappingHandlerMapping> allRequestMappings =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext,
                        RequestMappingHandlerMapping.class, true, false);

        for(RequestMappingHandlerMapping handlerMapping : allRequestMappings.values()) {
            Map<RequestMappingInfo, HandlerMethod> map = handlerMapping.getHandlerMethods();
            for(Map.Entry<RequestMappingInfo, HandlerMethod> me : map.entrySet()) {
                HandlerMethod handlerMethod = me.getValue();
                Class<?> clazz = handlerMethod.getBeanType();
                Object bean = applicationContext.getBean(clazz);
                if(uniqueBeanSet.add(bean)) {
                    ServiceDefinition serviceDefinition = HaafizAnnotationScanner.getInstance().scanBuilder(bean);
                    if(serviceDefinition != null) {
                        //	set environment
                        serviceDefinition.setEnvType(getEnv());
                        //	service registry definition
                        registerServiceDefinition(serviceDefinition);

                        //	service instance registry
                        ServiceInstance serviceInstance = new ServiceInstance();
                        String localIp = NetUtils.getLocalIp();
                        int port = serverProperties.getPort();
                        String serviceInstanceId = localIp + BasicConst.COLON_SEPARATOR + port;
                        String address = serviceInstanceId;
                        String uniqueId = serviceDefinition.getUniqueId();
                        String version = serviceDefinition.getVersion();

                        serviceInstance.setServiceInstanceId(serviceInstanceId);
                        serviceInstance.setUniqueId(uniqueId);
                        serviceInstance.setAddress(address);
                        serviceInstance.setWeight(HaafizConst.DEFAULT_WEIGHT);
                        serviceInstance.setRegisterTime(TimeUtil.currentTimeMillis());
                        serviceInstance.setVersion(version);

                        registerServiceInstance(serviceInstance);
                    }
                }
            }
        }
    }
}
