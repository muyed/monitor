package com.muye.monitor.agent.plugin;

import com.muye.monitor.agent.logger.Logger;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;

public class DaoProxyPluginTransformer implements MPluginTransformer {

    private static final Logger LOGGER = Logger.get(DaoProxyPluginTransformer.class);

    @Override
    public boolean matchTransformClass(MPlugin plugin) {
        return "org.apache.ibatis.binding.MapperProxy".equals(plugin.getClassName());
    }

    @Override
    public boolean matchTransformMethod(CtMethod method) {
        return Modifier.isPublic(method.getModifiers());
    }

    @Override
    public void transform(CtMethod method, MPlugin plugin) {
        try {
            CtClass etype = new ClassPool(true).get("java.lang.Throwable");
            String beforeCode = "{com.muye.monitor.agent.context.TrackContext.before();}";

            method.insertBefore(beforeCode);
            method.addCatch(daoCatchCode(), etype);
            method.insertAfter(daoAfterCode());

        } catch (Exception e) {
            LOGGER.error("cannot compile dao, method: {}", method.getLongName(), e);
        }
    }

    private String daoCatchCode(){
        return "{" +
                "com.muye.monitor.agent.content.MethodInfoContent _methodInfo = new com.muye.monitor.agent.content.MethodInfoContent();" +
                "_methodInfo.setClassName(this.mapperInterface.getSimpleName());" +
                "_methodInfo.setFullClassName(this.mapperInterface.getName());" +
                "_methodInfo.setMethodName($2.getName());" +
                "String _param = java.util.Arrays.toString($2.getParameterTypes()).substring(1);" +
                "_param = _param.substring(0, _param.length() - 1).replace(\"class \", \"\");" +
                "_methodInfo.setFullMethodName(this.mapperInterface.getName() + \".\" + $2.getName() + \"(\" + _param + \")\");" +
                "com.muye.monitor.agent.context.MethodInfoContext.set(_methodInfo);" +
                "com.muye.monitor.agent.context.MethodInfoContext.setErrMsg($e.getMessage());" +
                "com.muye.monitor.agent.mq.MqUtil.send(1);" +
                "throw $e;" +
                "}";
    }

    private String daoAfterCode(){
        return "{" +
                "com.muye.monitor.agent.content.MethodInfoContent _methodInfo = new com.muye.monitor.agent.content.MethodInfoContent();" +
                "_methodInfo.setClassName(this.mapperInterface.getSimpleName());" +
                "_methodInfo.setFullClassName(this.mapperInterface.getName());" +
                "_methodInfo.setMethodName($2.getName());" +
                "String _param = java.util.Arrays.toString($2.getParameterTypes()).substring(1);" +
                "_param = _param.substring(0, _param.length() - 1).replace(\"class \", \"\");" +
                "_methodInfo.setFullMethodName(this.mapperInterface.getName() + \".\" + $2.getName() + \"(\" + _param + \")\");" +
                "com.muye.monitor.agent.context.MethodInfoContext.set(_methodInfo);" +
                "com.muye.monitor.agent.mq.MqUtil.send(0);" +
                "}";
    }
}
