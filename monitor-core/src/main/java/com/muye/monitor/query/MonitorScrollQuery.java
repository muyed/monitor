package com.muye.monitor.query;

import org.elasticsearch.search.Scroll;

public class MonitorScrollQuery {

    private String scrollId;

    private Scroll scroll;

    private MonitorQuery query;

    public MonitorScrollQuery(MonitorQuery query){
        this.query = query;
    }

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }

    public Scroll getScroll() {
        return scroll;
    }

    public void setScroll(Scroll scroll) {
        this.scroll = scroll;
    }

    public MonitorQuery getQuery() {
        return query;
    }

    public void setQuery(MonitorQuery query) {
        this.query = query;
    }
}
