package com.haafiz.client.registry;

import com.haafiz.common.config.ServiceInstance;
import org.apache.dubbo.config.spring.ServiceBean;

import java.util.List;

/**
 * @author Anthony
 * @create 2022/1/15
 * @desc
 */
public interface DubboRegistry {

    /**
     * register {@link ServiceBean} from Dubbo
     *
     * @param serviceBean
     * @throws Exception
     */
    ServiceInstance registerServiceInstance(ServiceBean<?> serviceBean) throws Exception;
}
