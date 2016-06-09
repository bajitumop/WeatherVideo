package ru.example.makaroff.wheathervideo.io.rest.model;

import java.io.Serializable;

public class SystemParameters implements Serializable {
    protected int type;
    protected long id;
    protected float message;
    protected String country;
    protected long sunrise;
    protected long sunset;

    public long getSunset() {
        return sunset;
    }

    public long getSunrise() {
        return sunrise;
    }

    public String getCountry() {
        return country;
    }

    public float getMessage() {
        return message;
    }

    public long getId() {
        return id;
    }

    public int getType() {
        return type;
    }
}


