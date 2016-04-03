package com.planis.johannes.intelmeme.events;

/**
 * Created by JOHANNES on 3/31/2016.
 */
public class ImagePickedLocallyEvent {
    private int id;

    public ImagePickedLocallyEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
