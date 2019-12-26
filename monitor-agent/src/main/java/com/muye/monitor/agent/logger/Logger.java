package com.muye.monitor.agent.logger;

import com.muye.monitor.agent.lib.LibFactory;

import java.lang.reflect.Method;

public class Logger {

    private final Object libLogger;

    public Logger(Class clazz){
        libLogger = LibFactory.getLogger(clazz);
    }

    public static Logger get(Class clazz) {
        return new Logger(clazz);
    }

    public void info(String s, Object ... o) {
        try {
            Class[] paramType = new Class[]{s.getClass(), o.getClass()};
            Method method = libLogger.getClass().getMethod("info", paramType);

            Object[] params = new Object[] {s, o};
            method.invoke(libLogger, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void debug(String s, Object ... o){
        try {
            Class[] paramType = new Class[]{s.getClass(), o.getClass()};
            Method method = libLogger.getClass().getMethod("debug", paramType);

            Object[] params = new Object[] {s, o};
            method.invoke(libLogger, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void error(String s, Object ... o){
        try {
            Class[] paramType = new Class[]{s.getClass(), o.getClass()};
            Method method = libLogger.getClass().getMethod("error", paramType);

            Object[] params = new Object[] {s, o};
            method.invoke(libLogger, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
