package com.muye.monitor.agent.plugin;

import com.muye.monitor.agent.logger.Logger;
import javassist.CtMethod;

public class DubboProviderPluginTransformer implements MPluginTransformer {

    private static final Logger LOGGER = Logger.get(DubboProviderPluginTransformer.class);

    @Override
    public boolean matchTransformClass(MPlugin plugin) {
        return "com.alibaba.dubbo.rpc.filter.ContextFilter".equals(plugin.getClassName()) ||
                "org.apache.dubbo.rpc.filter.ContextFilter".equals(plugin.getClassName());
    }

    @Override
    public boolean matchTransformMethod(CtMethod method) {
        return method.getName().equals("invoke");
    }

    @Override
    public void transform(CtMethod method, MPlugin plugin) {
        String code = "{" +
                "String trackId = $2.getAttachment(\"trackId\");" +
                "String order = $2.getAttachment(\"order\");" +
                "String level = $2.getAttachment(\"level\");" +
                "TrackContext.init(trackId, order, level);" +
                "}";

        try {
            method.insertBefore(code);
            LOGGER.info("dubbo provider startup");
        } catch (Exception e) {
            LOGGER.error("cannot compile, dubbo provider", e);
        }

    }
}
