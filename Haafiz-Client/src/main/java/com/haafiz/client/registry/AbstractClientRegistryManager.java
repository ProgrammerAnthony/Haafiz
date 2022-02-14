package com.haafiz.client.registry;

import com.haafiz.client.autoconfig.HaafizProperties;
import com.haafiz.common.config.ServiceDefinition;
import com.haafiz.common.config.ServiceInstance;
import com.haafiz.common.constants.BasicConst;
import com.haafiz.common.util.FastJsonConvertUtil;
import com.haafiz.common.util.ServiceLoader;
import com.haafiz.discovery.api.Registry;
import com.haafiz.discovery.api.RegistryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


import java.io.InputStream;
import java.util.Properties;

/**
 * @author Anthony
 * @create 2022/1/15
 * @desc
 */
@Slf4j
public abstract class AbstractClientRegistryManager {

    public static final String PROPERTIES_PATH = "haafiz.properties";

    public static final String REGISTERYADDRESS_KEY = "registryAddress";

    public static final String NAMESPACE_KEY = "namespace";

    public static final String ENV_KEY = "env";

    protected volatile boolean whetherStart = false;

    public static Properties properties = new Properties();

    protected static String registryAddress ;

    protected static String namespace ;

    protected static String env ;

    protected static String superPath;

    protected static String servicesPath;

    protected static String instancesPath;

    protected static String rulesPath;

    private RegistryService registryService;

    //read from haafiz.properties
    static{
        InputStream in=null;
        AbstractClientRegistryManager.class.getClassLoader().getResourceAsStream(PROPERTIES_PATH);

        try {
            if(in != null) {
                properties.load(in);
                registryAddress = properties.getProperty(REGISTERYADDRESS_KEY);
                namespace = properties.getProperty(NAMESPACE_KEY);
                env = properties.getProperty(ENV_KEY);
                if(StringUtils.isBlank(registryAddress)) {
                    String errorMessage = "Haafiz service registry address cannot be empty";
                    log.error(errorMessage);
                    throw new RuntimeException(errorMessage);
                }
                if(StringUtils.isBlank(namespace)) {
                    namespace = HaafizProperties.HAAFIZ_PREFIX;
                }
            }


        } catch (Exception e) {
            log.error("#AbstractClientRegistryManager# InputStream load is error", e);
        }finally {
            if(in != null) {
                try {
                    in.close();
                } catch (Exception ex) {
                    //	ignore
                    log.error("#AbstractClientRegistryManager# InputStream close is error", ex);
                }
            }
        }
    }

   protected AbstractClientRegistryManager(HaafizProperties haafizProperties) throws Exception{
        //initialize load config
       if(haafizProperties.getRegistryAddress()!=null){
           registryAddress = haafizProperties.getRegistryAddress();
           namespace = haafizProperties.getNamespace();
           if(StringUtils.isBlank(namespace)) {
               namespace = HaafizProperties.HAAFIZ_PREFIX;
           }
           env = haafizProperties.getEnv();
       }
       //initialize register center object
       ServiceLoader<RegistryService> serviceLoader = ServiceLoader.load(RegistryService.class);
       for (RegistryService registryService : serviceLoader) {
           registryService.initialized(haafizProperties.getRegistryAddress());
           this.registryService = registryService;
       }

       //register top-level path
       generatorStructPath(Registry.PATH + namespace + BasicConst.BAR_SEPARATOR + env);

   }

    private void generatorStructPath(String path) throws Exception {
        superPath = path;
        registryService.registerPathIfNotExist(superPath, "", true);
        registryService.registerPathIfNotExist(servicesPath = superPath + Registry.SERVICE_PREFIX, "", true);
        registryService.registerPathIfNotExist(instancesPath = superPath + Registry.INSTANCE_PREFIX, "", true);
        registryService.registerPathIfNotExist(rulesPath = superPath + Registry.RULE_PREFIX, "", true);
    }



    /**
     * service registry definition
     * @param serviceDefinition
     * @throws Exception
     */
    protected void registerServiceDefinition(ServiceDefinition serviceDefinition) throws Exception {
        /**
         * 	/haafiz-env
         * 		/services
         * 			/serviceA:1.0.0  ==> ServiceDefinition
         * 			/serviceA:2.0.0
         * 			/serviceB:1.0.0
         * 		/instances
         * 			/serviceA:1.0.0/192.168.11.100:port	 ==> ServiceInstance
         * 			/serviceA:1.0.0/192.168.11.101:port
         * 			/serviceB:1.0.0/192.168.11.102:port
         * 			/serviceA:2.0.0/192.168.11.103:port
         * 		/rules
         * 			/ruleId1	==>	Rule
         * 			/ruleId2
         * 		/gateway
         */
        String key = servicesPath
                + Registry.PATH
                + serviceDefinition.getUniqueId();

        if(!registryService.isExistKey(key)) {
            String value = FastJsonConvertUtil.convertObjectToJSON(serviceDefinition);
            registryService.registerPathIfNotExist(key, value, true);
        }
    }


    /**
     * register service instance
     * @param serviceInstance
     * @throws Exception
     */
    protected void registerServiceInstance(ServiceInstance serviceInstance) throws Exception {
        String key = instancesPath
                + Registry.PATH
                + serviceInstance.getUniqueId()
                + Registry.PATH
                + serviceInstance.getServiceInstanceId();
        if(!registryService.isExistKey(key)) {
            String value = FastJsonConvertUtil.convertObjectToJSON(serviceInstance);
            registryService.registerPathIfNotExist(key, value, false);
        }
    }

    public static String getRegistryAddress() {
        return registryAddress;
    }

    public static String getNamespace() {
        return namespace;
    }

    public static String getEnv() {
        return env;
    }

    public static String getSuperPath() {
        return superPath;
    }

    public static String getServicesPath() {
        return servicesPath;
    }

    public static String getInstancesPath() {
        return instancesPath;
    }

    public static String getRulesPath() {
        return rulesPath;
    }

}
