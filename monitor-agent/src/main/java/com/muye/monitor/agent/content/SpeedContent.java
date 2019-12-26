package com.muye.monitor.agent.content;

public class SpeedContent {

    private Long start;

    private Long end;

    public void start(){
        start = System.currentTimeMillis();
    }

    public void end(){
        end = System.currentTimeMillis();
    }

    public Long getStart() {
        return start;
    }

    public Long getEnd() {
        return end;
    }


    public Long getSpeed() {
        if (start == null || end == null) {
            return null;
        }
        return end - start;
    }

}
