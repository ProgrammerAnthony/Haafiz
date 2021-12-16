package com.haafiz.core;

import com.haafiz.core.config.HaafizConfig;

/**
 * @author Anthony
 * @create 2021/12/16
 * @desc
 */
public class BootStrap {
    public static void main(String[] args) {
        //1 init config
        //2 plugin init
        //3 service registry
        //4 start container
        HaafizContainer haafizContainer=new HaafizContainer(new HaafizConfig());
        haafizContainer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }));
    }
}
