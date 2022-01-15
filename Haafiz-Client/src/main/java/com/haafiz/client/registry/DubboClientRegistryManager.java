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
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.spring.ServiceBean;
import org.apache.dubbo.config.spring.context.event.ServiceBeanExportedEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Anthony
 * @create 2022/1/15
 * @desc
 */
@Slf4j
public class DubboClientRegistryManager extends AbstractClientRegistryManager implements DubboRegistry, EnvironmentAware, ApplicationListener<ApplicationEvent> {
    private Environment environment;
    private static final Set<Object> uniqueBeanSet = new HashSet<>();

    @PostConstruct
    private void init() {
        String port = environment.getProperty(DubboConstants.DUBBO_PROTOCOL_PORT);
        if (StringUtils.isEmpty(port)) {
            log.error("Haafiz Dubbo service not start yet");
            return;
        }
        whetherStart = true;
    }

    public DubboClientRegistryManager(HaafizProperties haafizProperties) throws Exception {
        super(haafizProperties);
    }

    @Override
    public ServiceInstance registerServiceInstance(ServiceBean<?> serviceBean) throws Exception {
        Object bean = serviceBean.getRef();
        if (uniqueBeanSet.add(bean)) {
            ServiceDefinition serviceDefinition = HaafizAnnotationScanner.getInstance().scanBuilder(bean, serviceBean);
            if (serviceDefinition != null) {
                //	set environment
                serviceDefinition.setEnvType(getEnv());
                //	service registry definition
                registerServiceDefinition(serviceDefinition);

                //	service instance registry
                ServiceInstance serviceInstance = new ServiceInstance();
                String localIp = NetUtils.getLocalIp();
                int port = serviceBean.getProtocol().getPort();
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

                return serviceInstance;
            }
        }
        return null;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (!whetherStart) {
            return;
        }
        if (event instanceof ServiceBeanExportedEvent) {
            ServiceBean<?> serviceBean = ((ServiceBeanExportedEvent) event).getServiceBean();
            try {
                registerServiceInstance(serviceBean);
            } catch (Exception e) {
                log.error("Haafiz Dubbo service registry ServiceBean fail，ServiceBean = {}", serviceBean, e);
            }
        } else if (event instanceof ApplicationStartedEvent) {
            System.out.println("******************************************");
            System.out.println("**        Haafiz Dubbo Started           **");
            System.out.println("******************************************");
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
