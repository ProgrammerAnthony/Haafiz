package com.haafiz.etcd;

import io.etcd.jetcd.KeyValue;

/**
 * @author Anthony
 * @create 2022/2/14
 * @desc
 **/
public class EtcdChangedEvent {

    public static enum Type {
        PUT,
        DELETE,
        UNRECOGNIZED;
    }

    private KeyValue prevKeyValue;

    private KeyValue curtkeyValue;

    private Type type;

    public EtcdChangedEvent(KeyValue prevKeyValue, KeyValue curtkeyValue, Type type) {
        this.prevKeyValue = prevKeyValue;
        this.curtkeyValue = curtkeyValue;
        this.type = type;
    }

    public KeyValue getCurtkeyValue() {
        return curtkeyValue;
    }

    public KeyValue getPrevKeyValue() {
        return prevKeyValue;
    }

    public Type getType() {
        return type;
    }

}