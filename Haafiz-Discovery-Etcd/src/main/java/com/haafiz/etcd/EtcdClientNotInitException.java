package com.haafiz.etcd;

/**
 * @author Anthony
 * @create 2022/2/14
 * @desc
 **/
public class EtcdClientNotInitException extends RuntimeException {

    private static final long serialVersionUID = -617743243793838282L;

    public EtcdClientNotInitException() {
        super();
    }

    public EtcdClientNotInitException(String message) {
        super(message);
    }

}
