package com.muye.monitor.agent.plugin;

import javassist.CannotCompileException;
import javassist.CtClass;

import java.io.IOException;

public class MPlugin {

    private String className;

    private CtClass ctClass;

    private byte[] buffer;

    private ClassLoader loader;

    private MPlugin(){

    }

    public static MPlugin newInstance(String className, byte[] buffer, ClassLoader loader){
        className = className.replace("/", ".");
        MPlugin plugin = new MPlugin();
        plugin.className = className;
        plugin.buffer = buffer;
        plugin.loader = loader;
        return plugin;
    }

    public String getClassName(){
        return className;
    }

    public boolean hasCtClass() {
        return ctClass != null;
    }

    public CtClass getCtClass(){
        return ctClass;
    }

    public void setCtClass(CtClass ctClass){
        this.ctClass = ctClass;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public ClassLoader getLoader() {
        return loader;
    }

    public void setLoader(ClassLoader loader) {
        this.loader = loader;
    }

    public byte[] toBytes() throws CannotCompileException, IOException {
        if (hasCtClass()) {
            return ctClass.toBytecode();
        }

        return null;
    }
}
