package com.muye.monitor.agent.context;

import com.muye.monitor.agent.logger.Logger;
import com.muye.monitor.agent.content.SpeedContent;

import java.util.Stack;

public class SpeedContext {

    private static final Logger LOGGER = Logger.get(SpeedContext.class);

    private static final ThreadLocal<Stack<SpeedContent>> content = new ThreadLocal<>();

    public static void start(){
        Stack<SpeedContent> stack = content.get();
        if (stack == null) {
            content.set((stack = new Stack<>()));
        }

        SpeedContent speedContent = new SpeedContent();
        speedContent.start();
        stack.push(speedContent);
    }

    public static void end(){
        Stack<SpeedContent> stack = content.get();
        if (stack == null) {
            content.set((stack = new Stack<>()));
        }

        if (stack.isEmpty()) {
            return;
        }

        SpeedContent speedContent = stack.peek();
        if (speedContent != null) {
            speedContent.end();
        }
    }

    public static SpeedContent pop(){
        Stack<SpeedContent> stack = content.get();
        if (stack == null) {
            return new SpeedContent();
        }

        return stack.pop();
    }

    public static void clear(){
        Stack<SpeedContent> stack = content.get();
        if (stack != null && stack.empty()) {
            content.remove();
        }
    }
}
