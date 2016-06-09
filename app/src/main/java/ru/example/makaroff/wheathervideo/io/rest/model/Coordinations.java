package ru.example.makaroff.wheathervideo.io.rest.model;

import java.io.Serializable;

public class Coordinations implements Serializable{
    private double lon;
    private double lat;

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }
}
