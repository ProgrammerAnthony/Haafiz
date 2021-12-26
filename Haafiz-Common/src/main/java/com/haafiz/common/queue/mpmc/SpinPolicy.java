package com.haafiz.common.queue.mpmc;


public enum SpinPolicy {
    WAITING,
    BLOCKING,
    SPINNING;
}