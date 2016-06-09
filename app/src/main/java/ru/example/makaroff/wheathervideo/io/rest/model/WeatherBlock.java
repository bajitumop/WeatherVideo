package ru.example.makaroff.wheathervideo.io.rest.model;

public class WeatherBlock {
    protected long id;
    protected String main;
    protected String description;
    protected String icon;

    public long getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public String getMain() {
        return main;
    }
}
