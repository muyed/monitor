package com.muye.monitor.lib;

import org.slf4j.Logger;

public class LoggerFactory {

    public Logger getLogger(Class clazz){
        return org.slf4j.LoggerFactory.getLogger(clazz);
    }
}
