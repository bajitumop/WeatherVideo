package ru.example.makaroff.wheathervideo.io.rest.model.WeatherModel;

import java.io.Serializable;

public class MainParameters implements Serializable {

    private final float KELVIN_AMENDMENT = 273.15f;

    protected float temp;
    protected float pressure;
    protected float humidity;
    protected float temp_min;
    protected float temp_max;
    protected float sea_level;
    protected float grnd_level;

    public float getGrndLevel() {
        return grnd_level;
    }

    public float getSeaLevel() {
        return sea_level;
    }

    public int getMaxTemp() {
        return Math.round(temp_max - KELVIN_AMENDMENT);
    }

    public int getMinTemp() {
        return Math.round(temp_min - KELVIN_AMENDMENT);
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public int getTemp() {
        return Math.round(temp - KELVIN_AMENDMENT);
    }
}
