package com.haafiz.core;

import com.haafiz.common.util.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * @author Anthony
 * @create 2021/12/16
 * @desc
 * 网关配置加载规则：优先级顺序如下：高的优先级会覆盖掉低的优先级
 * 运行参数(最高) ->  jvm参数  -> 环境变量  -> 配置文件  -> 内部RapidConfig对象的默认属性值(最低);
 */
@Slf4j
public class HaafizConfigLoader {

    private final static String CONFIG_ENV_PREFIEX = "RAPID_";

    private final static String CONFIG_JVM_PREFIEX = "rapid.";

    private final static String CONFIG_FILE = "rapid.properties";

    private final static HaafizConfigLoader INSTANCE = new HaafizConfigLoader();

    private HaafizConfig haafizConfig = new HaafizConfig();

    private HaafizConfigLoader() {
    }

    public static HaafizConfigLoader getInstance() {
        return INSTANCE;
    }

    public static HaafizConfig getHaafizConfig() {
        return INSTANCE.haafizConfig;
    }

    public HaafizConfig load(String args[]) {

        //	1. load from config file
        {
            InputStream is = HaafizConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
            if(is != null) {
                Properties properties = new Properties();
                try {
                    properties.load(is);
                    PropertiesUtils.properties2Object(properties, haafizConfig);
                } catch (IOException e) {
                    //	warn
                    log.warn("#RapidConfigLoader# load config file: {} is error", CONFIG_FILE, e);
                } finally {
                    if(is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            //	ignore
                        }
                    }
                }
            }
        }

        //	2. load from environment config
        {
            Map<String, String> env = System.getenv();
            Properties properties = new Properties();
            properties.putAll(env);
            PropertiesUtils.properties2Object(properties, haafizConfig, CONFIG_ENV_PREFIEX);
        }

        //	3. load from jvm config
        {
            Properties properties = System.getProperties();
            PropertiesUtils.properties2Object(properties, haafizConfig, CONFIG_JVM_PREFIEX);
        }

        //	4. load from running config: --xxx=xxx --enable=true  --port=1234
        {
            if(args != null && args.length > 0) {
                Properties properties = new Properties();
                for(String arg : args) {
                    if(arg.startsWith("--") && arg.contains("=")) {
                        properties.put(arg.substring(2, arg.indexOf("=")), arg.substring(arg.indexOf("=") + 1));
                    }
                }
                PropertiesUtils.properties2Object(properties, haafizConfig);
            }
        }

        return haafizConfig;
    }

}
