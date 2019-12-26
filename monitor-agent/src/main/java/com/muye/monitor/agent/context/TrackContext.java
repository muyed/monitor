package com.muye.monitor.agent.context;

import com.muye.monitor.agent.logger.Logger;
import com.muye.monitor.agent.content.TrackContent;

import java.util.Stack;

public class TrackContext {

    private static final Logger LOGGER = Logger.get(TrackContext.class);

    private static final ThreadLocal<Stack<TrackContent>> content = new ThreadLocal<>();
    private static final ThreadLocal<Integer> order = new ThreadLocal<>();

    public static void before(){
        Stack<TrackContent> stack = content.get();
        if (stack == null) {
            content.set((stack = new Stack<>()));
        }

        TrackContent trackContent = stack.empty() ? new TrackContent() : new TrackContent(stack.peek());

        stack.push(trackContent);
    }

    public static TrackContent pop(){
        Stack<TrackContent> stack = content.get();
        if (stack == null) {
            return new TrackContent();
        }

        return stack.pop();
    }

    public static Integer getAndAddOrder(){
        Integer i = order.get();
        if (i == null) {
            i = 0;
        }

        order.set(++i);

        return i;
    }

    public static String getTrackId(){
        Stack<TrackContent> stack = getStack();
        if (stack == null || stack.empty()) {
            return null;
        }

        TrackContent trackContent = stack.peek();
        return trackContent.getTrackId();
    }

    public static String getTrackOrder(){
        Stack<TrackContent> stack = getStack();
        if (stack == null || stack.empty()) {
            return null;
        }

        TrackContent trackContent = stack.peek();
        return trackContent.getTrackOrder() == null ? null : trackContent.getTrackOrder() + "";
    }

    public static String getTrackLevel(){
        Stack<TrackContent> stack = getStack();
        if (stack == null || stack.empty()) {
            return null;
        }

        TrackContent trackContent = stack.peek();
        return trackContent.getTrackLevel() == null ? null : trackContent.getTrackLevel() + "";
    }

    public static void init(String trackId, String order, String level){
        if (trackId == null || trackId.length() == 0) {
            return;
        }

        Integer o = 0;

        if (order != null && order.length() > 0) {
            o = Integer.valueOf(order);
        }

        Integer l = 0;

        if (level != null && level.length() > 0) {
            l = Integer.valueOf(level);
        }

        TrackContent trackContent = new TrackContent(trackId, o, l);
        Stack<TrackContent> stack = new Stack<>();
        stack.push(trackContent);
        setStack(stack);
        setOrder(trackContent.getTrackOrder());
    }

    public static Stack<TrackContent> getStack(){
        return content.get();
    }

    public static Integer getOrder(){
        return order.get();
    }

    public static void setStack(Stack<TrackContent> stack){
        content.set(stack);
    }

    public static void setOrder(Integer trackOrder){
        order.set(trackOrder);
    }

    public static void clear(){
        Stack<TrackContent> stack = content.get();
        if (stack != null && stack.empty()) {
            content.remove();
            order.remove();
        }
    }
}
