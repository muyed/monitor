package com.muye.monitor.agent.content;

import com.muye.monitor.agent.context.TrackContext;
import com.muye.monitor.common.util.SntKeyGenerator;

public class TrackContent {

    private final String trackId;

    private Integer trackOrder;

    private Integer trackLevel;

    public TrackContent (){
        trackId = SntKeyGenerator.getInstance().generateKey().toString();
        trackLevel = 1;
        trackOrder = TrackContext.getAndAddOrder();
    }

    public TrackContent(TrackContent front){
        trackId = front.getTrackId();
        trackLevel = front.getTrackLevel() + 1;
        trackOrder = TrackContext.getAndAddOrder();
    }

    public TrackContent(String trackId, Integer trackOrder, Integer trackLevel){
        this.trackId = trackId;
        this.trackOrder = trackOrder;
        this.trackLevel = trackLevel;
    }

    public String getTrackId() {
        return trackId;
    }

    public Integer getTrackOrder() {
        return trackOrder;
    }

    public Integer getTrackLevel() {
        return trackLevel;
    }


}
