package com.muye.monitor.common;

import java.util.List;

public class MonitorTrackResult extends MonitorResult {

    private List<MonitorTrackResult> lowers;

    public List<MonitorTrackResult> getLowers() {
        return lowers;
    }

    public void setLowers(List<MonitorTrackResult> lowers) {
        this.lowers = lowers;
    }
}
