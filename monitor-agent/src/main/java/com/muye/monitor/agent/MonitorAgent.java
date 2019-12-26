package com.muye.monitor.agent;

import com.muye.monitor.agent.logger.Logger;
import com.muye.monitor.agent.plugin.*;
import javassist.CtMethod;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

public class MonitorAgent {

    private static final Logger LOGGER = Logger.get(MonitorAgent.class);

    private static List<MPluginTransformer> transformers = new ArrayList<>();

    public static void premain(String agentArgs, Instrumentation inst) {

        LOGGER.info("monitor agent startup ...");

        addTransformers();

        inst.addTransformer((loader, className, redefined, domain, buffer) -> {

            if (className == null || className.length() == 0) {
                return null;
            }

            MPlugin plugin = MPlugin.newInstance(className, buffer, loader);
            for (MPluginTransformer transformer : transformers) {
                if (transformer.isTransformClass(plugin)) {
                    for (CtMethod method : plugin.getCtClass().getDeclaredMethods()) {
                        if (transformer.matchTransformMethod(method)) {
                            transformer.transform(method, plugin);
                        }
                    }
                }
            }

            try {
                return plugin.toBytes();
            } catch (Exception e) {
                LOGGER.error("transform plugin failed, className: {}", className, e);
            }

            return buffer;
        });

        LOGGER.info("monitor agent startup success");
    }

    public static void addTransformers() {
        transformers.add(new SpeedPluginTransformer());
        transformers.add(new TrackAndSendPluginTransformer());
        transformers.add(new DaoProxyPluginTransformer());
        transformers.add(new DubboConsumerPluginTransformer());
        transformers.add(new DubboProviderPluginTransformer());
    }
}
