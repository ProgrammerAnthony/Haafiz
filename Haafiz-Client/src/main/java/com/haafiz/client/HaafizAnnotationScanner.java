package com.haafiz.client;

import com.haafiz.client.registry.DubboConstants;
import com.haafiz.common.config.DubboServiceInvoker;
import com.haafiz.common.config.HttpServiceInvoker;
import com.haafiz.common.config.ServiceDefinition;
import com.haafiz.common.config.ServiceInvoker;
import com.haafiz.common.constants.BasicConst;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.config.spring.ServiceBean;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Anthony
 * @create 2022/1/15
 * @desc
 */
public class HaafizAnnotationScanner {

    private HaafizAnnotationScanner() {

    }

    private static class SingletonHolder {
        private static HaafizAnnotationScanner INSTANCE = new HaafizAnnotationScanner();
    }

    public HaafizAnnotationScanner getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * scan the incoming bean and convert it to {@link ServiceDefinition}
     *
     * @param bean
     * @param args
     * @return
     */
    public synchronized ServiceDefinition scanBuilder(Object bean, Object... args) {
        Class<?> clazz = bean.getClass();
        boolean isPresent = clazz.isAnnotationPresent(HaafizService.class);
        if (isPresent) {
            HaafizService haafizService = clazz.getAnnotation(HaafizService.class);
            String serviceId = haafizService.serviceId();
            HaafizProtocol protocol = haafizService.protocol();
            String patternPath = haafizService.patternPath();
            String version = haafizService.version();
            ServiceDefinition serviceDefinition = new ServiceDefinition();
            Map<String /* invokerPath */, ServiceInvoker> invokerMap = new HashMap<String, ServiceInvoker>();
            Method[] methods = clazz.getMethods();
            if (methods != null && methods.length > 0) {
                for (Method method : methods) {
                    HaafizInvoker invoker = method.getAnnotation(HaafizInvoker.class);
                    if(invoker == null) {
                        continue;
                    }
                    String path = invoker.path();
                    switch (protocol){
                        case HTTP:
                            HttpServiceInvoker httpServiceInvoker = createHttpServiceInvoker(path, bean, method);
                            invokerMap.put(path, httpServiceInvoker);
                        case DUBBO:
                            ServiceBean<?> serviceBean = (ServiceBean<?>)args[0];
                            DubboServiceInvoker dubboServiceInvoker = createDubboServiceInvoker(path, serviceBean, method);
                            //	dubbo version reset for serviceDefinition version
                            String dubboVersion = dubboServiceInvoker.getVersion();
                            if(!StringUtils.isBlank(dubboVersion)) {
                                version = dubboVersion;
                            }
                            invokerMap.put(path, dubboServiceInvoker);
                            break;
                        default:
                            break;
                    }
                    //	设置属性
                    serviceDefinition.setUniqueId(serviceId + BasicConst.COLON_SEPARATOR + version);
                    serviceDefinition.setServiceId(serviceId);
                    serviceDefinition.setVersion(version);
                    serviceDefinition.setProtocol(protocol.getProtocolName());
                    serviceDefinition.setPatternPath(patternPath);
                    serviceDefinition.setEnable(true);
                    serviceDefinition.setInvokerMap(invokerMap);
                    return serviceDefinition;
                }
            }
        }
        return null;
    }

    private DubboServiceInvoker createDubboServiceInvoker(String path, ServiceBean<?> serviceBean, Method method) {
        DubboServiceInvoker dubboServiceInvoker = new DubboServiceInvoker();
        dubboServiceInvoker.setInvokerPath(path);

        String methodName = method.getName();
        String registerAddress = serviceBean.getRegistry().getAddress();
        String interfaceClass = serviceBean.getInterface();

        dubboServiceInvoker.setRegisterAddress(registerAddress);
        dubboServiceInvoker.setMethodName(methodName);
        dubboServiceInvoker.setInterfaceClass(interfaceClass);

        String[] parameterTypes = new String[method.getParameterCount()];
        Class<?>[] classes = method.getParameterTypes();
        for(int i = 0; i < classes.length; i ++) {
            parameterTypes[i] = classes[i].getName();
        }
        dubboServiceInvoker.setParameterTypes(parameterTypes);

        Integer seriveTimeout = serviceBean.getTimeout();
        if(seriveTimeout == null || seriveTimeout == 0) {
            ProviderConfig providerConfig = serviceBean.getProvider();
            if(providerConfig != null) {
                Integer providerTimeout = providerConfig.getTimeout();
                if(providerTimeout == null || providerTimeout == 0) {
                    seriveTimeout = DubboConstants.DUBBO_TIMEOUT;
                } else {
                    seriveTimeout = providerTimeout;
                }
            }
        }
        dubboServiceInvoker.setTimeout(seriveTimeout);

        String dubboVersion = serviceBean.getVersion();
        dubboServiceInvoker.setVersion(dubboVersion);

        return dubboServiceInvoker;
    }

    private HttpServiceInvoker createHttpServiceInvoker(String path, Object bean, Method method) {
        HttpServiceInvoker httpServiceInvoker = new HttpServiceInvoker();
        httpServiceInvoker.setInvokerPath(path);
        return httpServiceInvoker;
    }
}
