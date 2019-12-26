package com.muye.monitor.agent.lib;

import com.muye.monitor.common.constant.Constant;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class LibFactory {

    private static Object loggerFactory;

    static {
        try {
            Class factoryClass = LibClassLoader.getLibClassLoader().loadClass("com.muye.monitor.lib.LoggerFactory");
            loggerFactory = factoryClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static Object getLogger (Class clazz) {

        try {
            Method method = loggerFactory.getClass().getMethod("getLogger", Class.class);
            return method.invoke(loggerFactory, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static Object getSmqProducer(){
        try {
            Class clazz = LibClassLoader.getLibClassLoader().loadClass("com.muye.monitor.lib.MqProducer");
            Class[] paramsType = new Class[] {String.class, String.class};
            Constructor constructor = clazz.getConstructor(paramsType);

            Object[] params = new Object[]{Constant.getMqAddr(), Constant.MQ_GROUP};
            return constructor.newInstance(params);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
