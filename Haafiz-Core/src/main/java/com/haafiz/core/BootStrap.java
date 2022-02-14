package com.haafiz.core;

/**
 * @author Anthony
 * @create 2021/12/16
 * @desc
 */
public class BootStrap {
    public static void main(String[] args) {
        //1 init config
        HaafizConfig haafizConfig = HaafizConfigLoader.getInstance().load(args);
        //2 todo plugin init
        //3 todo service registry
        //4 todo start container
        HaafizContainer haafizContainer = new HaafizContainer(new HaafizConfig());
        haafizContainer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                haafizContainer.stop();
            }
        }));
    }
}
