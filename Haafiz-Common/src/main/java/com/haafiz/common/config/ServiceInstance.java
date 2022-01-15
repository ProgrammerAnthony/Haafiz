package com.haafiz.common.config;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Anthony
 * @create 2021/12/16
 * @desc one service with multiple instances({@link ServiceInstance})
 */
public class ServiceInstance implements Serializable {

    private static final long serialVersionUID = -7559569289189228478L;

    /**
     * service instance id = ip:port
     */
    private String serviceInstanceId;

    /**
     * service uniqueId
     */
    private String uniqueId;

    /**
     * service registry address： ip:port
     */
    private String address;


    private String tags;

    private Integer weight;

    /**
     * service registry time , for later load balance and warm up
     */
    private long registerTime;


    private boolean enable = true;

    /**
     * service instance version
     */
    private String version;

    public ServiceInstance() {
        super();
    }

    public ServiceInstance(String serviceInstanceId, String uniqueId, String address, String tags, Integer weight,
                           long registerTime, boolean enable, String version) {
        super();
        this.serviceInstanceId = serviceInstanceId;
        this.uniqueId = uniqueId;
        this.address = address;
        this.tags = tags;
        this.weight = weight;
        this.registerTime = registerTime;
        this.enable = enable;
        this.version = version;
    }

    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    public void setServiceInstanceId(String serviceInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this == null || getClass() != o.getClass()) return false;
        ServiceInstance serviceInstance = (ServiceInstance) o;
        return Objects.equals(serviceInstanceId, serviceInstance.serviceInstanceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceInstanceId);
    }

}
