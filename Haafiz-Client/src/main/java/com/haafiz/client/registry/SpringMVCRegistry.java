package com.haafiz.client.registry;

import com.haafiz.common.config.ServiceInstance;
import org.apache.dubbo.config.spring.ServiceBean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;

/**
 * @author Anthony
 * @create 2022/1/15
 * @desc
 */
public interface SpringMVCRegistry{

    /**
     * register {@link RequestMappingHandlerMapping} from Spring MVC
     * @param allRequestMappings
     * @return
     * @throws Exception
     */
    List<ServiceInstance> registerServiceInstance(Map<String, RequestMappingHandlerMapping> allRequestMappings) throws Exception;
}
