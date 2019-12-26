package com.muye.monitor.agent.context;

import com.muye.monitor.agent.content.MethodInfoContent;

public class MethodInfoContext {

    private static final ThreadLocal<MethodInfoContent> content = new ThreadLocal<>();

    public static void set(MethodInfoContent methodInfoContent){
        content.set(methodInfoContent);
    }

    public static MethodInfoContent get(){
        return content.get() == null ? new MethodInfoContent() : content.get();
    }

    public static void setErrMsg (String msg) {
        MethodInfoContent methodInfoContent = content.get();
        if (methodInfoContent == null) {
            content.set((methodInfoContent = new MethodInfoContent()));
        }

        methodInfoContent.setErrMsg(msg);
    }

    public static void clear(){
        content.remove();
    }
}
