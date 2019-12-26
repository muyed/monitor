package com.muye.monitor.agent.plugin;

import com.muye.monitor.agent.logger.Logger;
import javassist.*;

public class SpeedPluginTransformer implements MPluginTransformer {

    private static final Logger LOGGER = Logger.get(SpeedPluginTransformer.class);

    @Override
    public boolean matchTransformClass(MPlugin plugin) {
        if (plugin == null) {
            return false;
        }
        return matchSongshuClass(plugin.getClassName()) || "org.apache.ibatis.binding.MapperProxy".equals(plugin.getClassName());
    }

    @Override
    public boolean matchTransformMethod(CtMethod method) {
        int i = method.getModifiers();
        return Modifier.isPublic(i) && !Modifier.isAbstract(i) && !Modifier.isStatic(i);
    }

    @Override
    public void transform(CtMethod method, MPlugin plugin) {
        try {
            CtClass etype = new ClassPool(true).get("java.lang.Throwable");

            method.insertBefore("com.muye.monitor.agent.context.SpeedContext.start();");
            method.addCatch("{com.muye.monitor.agent.context.SpeedContext.end(); throw $e;}", etype);
            method.insertAfter("com.muye.monitor.agent.context.SpeedContext.end();");
        } catch (CannotCompileException | NotFoundException e ) {
            LOGGER.error("cannot compile, method: {}", method.getLongName(), e);
        }
    }
}
