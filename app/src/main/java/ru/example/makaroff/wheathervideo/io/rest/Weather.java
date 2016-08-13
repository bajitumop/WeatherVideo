package ru.example.makaroff.wheathervideo.io.rest;

import java.io.Serializable;

import ru.example.makaroff.wheathervideo.io.rest.model.WeatherModel.Clouds;
import ru.example.makaroff.wheathervideo.io.rest.model.WeatherModel.Coordinations;
import ru.example.makaroff.wheathervideo.io.rest.model.WeatherModel.MainParameters;
import ru.example.makaroff.wheathervideo.io.rest.model.WeatherModel.Rain;
import ru.example.makaroff.wheathervideo.io.rest.model.WeatherModel.Snow;
import ru.example.makaroff.wheathervideo.io.rest.model.WeatherModel.SystemParameters;
import ru.example.makaroff.wheathervideo.io.rest.model.WeatherModel.WeatherBlock;
import ru.example.makaroff.wheathervideo.io.rest.model.WeatherModel.Wind;

public class Weather implements Serializable{

    protected Coordinations coord;
    protected MainParameters main;
    protected String base;
    protected WeatherBlock[] weather;
    protected Wind wind;
    protected Clouds clouds;
    protected Rain rain;
    protected Snow snow;
    protected long dt;
    protected SystemParameters sys;
    protected String name;
    protected long id;
    protected String cod;
    protected String message;
    protected boolean isSuccess;

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getCod(){
        return cod;
    }

    public Coordinations getCoord() {
        return coord;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public WeatherBlock[] getWeatherBlock() {
        return weather;
    }

    public SystemParameters getSys() {
        return sys;
    }

    public long getDt() {
        return dt;
    }

    public Snow getSnow() {
        return snow;
    }

    public Rain getRain() {
        return rain;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public String getBase() {
        return base;
    }

    public MainParameters getMain() {
        return main;
    }

    public WeatherBlock getWeatherBlockAtPosition(short index) {
        return weather[index];
    }
}
