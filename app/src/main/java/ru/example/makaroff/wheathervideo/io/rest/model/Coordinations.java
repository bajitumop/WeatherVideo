package ru.example.makaroff.wheathervideo.io.rest.model;

public class Coordinations {

    protected float lon;

    protected float lat;

    public Coordinations(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public float getLon() {
        return lon;
    }

    public float getLat() {
        return lat;
    }
}
