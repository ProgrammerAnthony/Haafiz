package com.haafiz.client;

/**
 * @author Anthony
 * @create 2022/1/15
 * @desc
 */
public enum HaafizProtocol {
    DUBBO("dubbo", "dubbo protocol"),

    HTTP("http", "http protocol");

    String protocolName;
    String protocolDesc;

    HaafizProtocol(String protocolName, String protocolDesc) {
        this.protocolName = protocolName;
        this.protocolDesc = protocolDesc;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public String getProtocolDesc() {
        return protocolDesc;
    }
}
