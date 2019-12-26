package com.muye.monitor.agent.plugin;

import com.muye.monitor.agent.logger.Logger;
import javassist.CtMethod;

public class DubboConsumerPluginTransformer implements MPluginTransformer {

    private static final Logger LOGGER = Logger.get(DubboConsumerPluginTransformer.class);

    @Override
    public boolean matchTransformClass(MPlugin plugin) {
        return plugin.getClassName().equals("com.alibaba.dubbo.rpc.filter.ConsumerContextFilter") ||
                plugin.getClassName().equals("org.apache.dubbo.rpc.filter.ConsumerContextFilter");
    }

    @Override
    public boolean matchTransformMethod(CtMethod method) {
        return method.getName().equals("invoke");
    }

    @Override
    public void transform(CtMethod method, MPlugin plugin) {
        String code = "{" +
                "String trackId = TrackContext.getTrackId();" +
                "String order = TrackContext.getTrackOrder();" +
                "String level = TrackContext.getTrackLevel();" +
                "$2.getAttachments().put(\"trackId\", trackId);" +
                "$2.getAttachments().put(\"order\", order);" +
                "$2.getAttachments().put(\"level\", level);" +
                "}";

        try {
            method.insertBefore(code);
            LOGGER.info("dubbo consumer plugin startup");
        } catch (Exception e) {
            LOGGER.error("cannot compile, dubbo consumer", e);
        }
    }
}
