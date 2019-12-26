package com.muye.monitor.agent.plugin;

import com.muye.monitor.agent.logger.Logger;
import javassist.*;

public class TrackAndSendPluginTransformer implements MPluginTransformer {

    private static final Logger LOGGER = Logger.get(TrackAndSendPluginTransformer.class);

    @Override
    public boolean matchTransformClass(MPlugin plugin) {
        if (plugin == null) {
            return false;
        }

        return matchSongshuClass(plugin.getClassName());
    }

    @Override
    public boolean matchTransformMethod(CtMethod method) {
        int i = method.getModifiers();
        return Modifier.isPublic(i) && !Modifier.isAbstract(i) && !Modifier.isStatic(i);
    }

    @Override
    public void transform(CtMethod method, MPlugin plugin) {
        try {
            String beforeCode = "{com.muye.monitor.agent.context.TrackContext.before();}";
            CtClass etype = new ClassPool(true).get("java.lang.Throwable");

            method.insertBefore(beforeCode);
            method.addCatch(defaultCatchCode(plugin.getCtClass(), method), etype);
            method.insertAfter(defaultAfterCode(plugin.getCtClass(), method));
        } catch (CannotCompileException | NotFoundException e) {
            LOGGER.error("cannot compile, method: {}", method.getLongName(), e);
        }
    }

    private String defaultCatchCode (CtClass ctClass, CtMethod method){
        return "{" +
                "com.muye.monitor.agent.content.MethodInfoContent _methodInfo = new MethodInfoContent();" +
                "_methodInfo.setClassName(\"" + ctClass.getSimpleName() + "\");" +
                "_methodInfo.setFullClassName(\"" + ctClass.getName() + "\");" +
                "_methodInfo.setMethodName(\"" + method.getName() + "\");" +
                "_methodInfo.setFullMethodName(\"" + method.getLongName() + "\");" +
                "com.muye.monitor.agent.content.MethodInfoContext.set(_methodInfo);" +
                "com.muye.monitor.agent.content.MethodInfoContext.setErrMsg($e.getMessage());" +
                "com.muye.monitor.agent.mq.MqUtil.send(1);" +
                "throw $e;" +
                "}";
    }

    private String defaultAfterCode(CtClass ctClass, CtMethod method){
        return "{" +
                "MethodInfoContent _methodInfo = new MethodInfoContent();" +
                "_methodInfo.setClassName(\"" + ctClass.getSimpleName() + "\");" +
                "_methodInfo.setFullClassName(\"" + ctClass.getName() + "\");" +
                "_methodInfo.setMethodName(\"" + method.getName() + "\");" +
                "_methodInfo.setFullMethodName(\"" + method.getLongName() + "\");" +
                "MethodInfoContext.set(_methodInfo);" +
                "MqUtil.send(0);" +
                "}";
    }
}
