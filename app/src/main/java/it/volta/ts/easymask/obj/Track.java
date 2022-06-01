package it.volta.ts.easymask.obj;

import java.util.ArrayList;
import java.util.List;

public class Track{
    private List<FPoint> trackList;
    private boolean eraser;
    private float stroke;

    public Track(){
        trackList = new ArrayList<>();
    }

    public List<FPoint> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<FPoint> trackList) {
        this.trackList = trackList;
    }

    public boolean isEraser() {
        return eraser;
    }

    public void setEraser(boolean eraser) {
        this.eraser = eraser;
    }

    public float getStroke() {
        return stroke;
    }

    public void setStroke(float stroke) {
        this.stroke = stroke;
    }
}
