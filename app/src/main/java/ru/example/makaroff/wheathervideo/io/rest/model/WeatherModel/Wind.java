package ru.example.makaroff.wheathervideo.io.rest.model.WeatherModel;

import java.io.Serializable;

public class Wind implements Serializable {
    protected float speed;
    protected float deg;

    public float getDeg() {
        return deg;
    }

    public float getSpeed() {
        return speed;
    }
}
