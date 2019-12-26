package com.muye.monitor.agent.plugin;

import com.muye.monitor.agent.logger.Logger;
import com.muye.monitor.common.constant.Constant;
import javassist.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface MPluginTransformer {

    Logger LOGGER = Logger.get(MPluginTransformer.class);

    /**
     * 是否类需要转换成插件
     * @return
     */
    boolean matchTransformClass(MPlugin plugin);

    boolean matchTransformMethod(CtMethod method);

    /**
     * 转换成插件
     * @return
     */
    void transform(CtMethod method, MPlugin plugin);

    default boolean isTransformClass(MPlugin plugin) {
        boolean b = matchTransformClass(plugin);
        if (!plugin.hasCtClass() && b) {
            ClassPool cp = new ClassPool(true);
            try {
                cp.insertClassPath(new LoaderClassPath(plugin.getLoader()));
                CtClass ctClass = cp.get(plugin.getClassName());
                if (ctClass.isInterface()) {
                    return false;
                }
                plugin.setCtClass(ctClass);
            } catch (Exception e) {
            }
        }

        return b;
    }

    default boolean matchSongshuClass(String className){
        if (className == null || className.endsWith("OkController")) {
            return false;
        }

        Pattern r = Pattern.compile(Constant.SONGSHU_PACKAGE);
        Matcher m = r.matcher(className);

        return m.matches();
    }
}
