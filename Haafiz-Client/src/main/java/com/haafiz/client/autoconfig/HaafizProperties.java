package com.haafiz.client.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Anthony
 * @create 2022/1/15
 * @desc
 */
@Data
@ConfigurationProperties(prefix = HaafizProperties.HAAFIZ_PREFIX)
public class HaafizProperties {

    public final static String HAAFIZ_PREFIX = "haafiz";

    private String registryAddress;

    private String namespace = HAAFIZ_PREFIX;

    private String env = "dev";
}
